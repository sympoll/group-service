package com.MTAPizza.Sympoll.groupmanagementservice.service;

import com.MTAPizza.Sympoll.groupmanagementservice.client.MediaClient;
import com.MTAPizza.Sympoll.groupmanagementservice.client.PollClient;
import com.MTAPizza.Sympoll.groupmanagementservice.client.UserClient;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.decription.GroupUpdateDescriptionRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.request.delete.GroupDataDeleteRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.response.update.GroupUpdateProfileBannerUrlResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.response.update.GroupUpdateProfilePictureUrlResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.poll.DeleteGroupPollsRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.request.update.GroupUpdateProfileBannerUrlRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.request.update.GroupUpdateProfilePictureUrlRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service.GroupNameResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service.GroupResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service.MemberDetailsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service.MemberResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.media.service.GroupDataDeleteResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.poll.service.DeleteGroupPollsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.group.GroupNotFoundException;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.request.RequestFailedException;
import com.MTAPizza.Sympoll.groupmanagementservice.model.Group;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
import com.MTAPizza.Sympoll.groupmanagementservice.model.role.RoleName;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service.UserDataResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.GroupRepository;
import com.MTAPizza.Sympoll.groupmanagementservice.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberService memberService;
    private final UserRolesService userRolesService;
    private final MediaClient mediaClient;
    private final UserClient userClient;
    private final PollClient pollClient;
    private final Validator validator;

    /**
     * Create and add a group to the database.
     * @param groupCreateRequest Details of the group to add.
     * @return Details of the group that was added to the database.
     */
    @Transactional
    public GroupResponse createGroup(GroupCreateRequest groupCreateRequest) {
        validator.validateCreateNewGroup(groupCreateRequest);
        UserDataResponse userDataResponse = getUserDataById(groupCreateRequest.creatorId());

        log.info("Creating group {}", groupCreateRequest);
        String fixedSpacesGroupName = groupCreateRequest.groupName().toLowerCase().replace(" ", "-");

        Group createdGroup = Group.builder()
                .groupId(!groupRepository.existsById(fixedSpacesGroupName) ? fixedSpacesGroupName : UUID.randomUUID().toString().replaceAll("[^0-9]", "")) // If defined a group ID then use it, otherwise generate random group ID.
                .groupName(groupCreateRequest.groupName())
                .description(groupCreateRequest.description())
                .creatorId(groupCreateRequest.creatorId())
                .build();

        groupRepository.save(createdGroup);
        log.info("Group with ID - '{}' was created by - '{}'", createdGroup.getGroupId(), createdGroup.getCreatorId());

        // Create Member object for the group creator
        Member creator = new Member(createdGroup.getGroupId(), createdGroup.getCreatorId());
        memberService.createNewMember(creator, userDataResponse, RoleName.ROLE_ADMIN.toString());
        createdGroup.addMember(creator);
        log.info("User with ID - '{}' added to the new group as a member", creator.getUserId());

        // Set the creator to be group admin
        userRolesService.createUserRole(createdGroup.getCreatorId(), createdGroup.getGroupId(), RoleName.ROLE_ADMIN.toString());
        log.info("User with ID - '{}' set as admin in the new group", creator.getUserId());

        groupRepository.save(createdGroup);
        log.info("Group with ID - '{}' was updated with new member - '{}'", createdGroup.getGroupId(), creator.getUserId());

        return createdGroup.toGroupResponse();
    }


    /**
     * Add a new member to a group in the database.
     * @param groupId ID of the group to add the member to.
     * @param username The user's username to add to the group.
     * @return  Information on the member that created and added to the group.
     */
    @Transactional
    public MemberDetailsResponse addMember(String groupId, String username) {
        UserDataResponse userDataResponse = getUserDataByUsername(username);
        validator.validateAddMember(groupId, userDataResponse.userId());

        Member newMember = new Member(groupId, userDataResponse.userId());
        Group group = groupRepository.getReferenceById(groupId);

        group.addMember(newMember);
        groupRepository.save(group);
        return memberService.createNewMember(newMember, userDataResponse, RoleName.ROLE_MEMBER.toString());
    }


    /**
     * Fetch a user's data from User-Service, by his username.
     * @param username Username of the user to fetch his data.
     * @return Data of the user fetched.
     */
    private UserDataResponse getUserDataByUsername(String username) {
        log.info("Sending request to get user data of user with Username: '{}' from the user service", username);
        ResponseEntity<UserDataResponse> response = userClient.getUserDataByUsername(username);

        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else {
            String errorMessage = response.hasBody() ? String.valueOf(response.getBody()) : "No error message in the response body";
            log.error("Request to user service failed. Status code {}", response.getStatusCode());
            throw new RequestFailedException("Request to user service failed. Status code " + response.getStatusCode() + "error message " + errorMessage);

        }
    }

    /**
     * Fetch a user's data from User-Service, by his ID.
     * @param id ID of the user to fetch his data.
     * @return Data of the user fetched.
     */
    private UserDataResponse getUserDataById(UUID id) {
        log.info("Sending request to get user data of user with ID: '{}' from the user service", id);
        ResponseEntity<UserDataResponse> response = userClient.getUserDataById(id);

        if(response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        } else {
            String errorMessage = response.hasBody() ? String.valueOf(response.getBody()) : "No error message in the response body";
            log.error("Request to user service failed. Status code {}", response.getStatusCode());
            throw new RequestFailedException("Request to user service failed. Status code " + response.getStatusCode() + "error message " + errorMessage);
        }
    }

    /**
     * Remove a member from a group in the database.
     * @param groupId ID of the group to remove the member from.
     * @param userId ID of the user to remove as a member.
     * @return  Information on the member that was removed from the group.
     */
    @Transactional
    public MemberResponse removeMember(String groupId, UUID userId) {
        validator.validateRemoveMember(groupId, userId);
        Group group = groupRepository.getReferenceById(groupId);
        Member memberToRemove = group.getMember(userId);
        String userRoleName = userRolesService.getRoleNameOfSpecificUser(userId, groupId);

        if (group.getMembersList().size() == 1) {
            deleteGroup(groupId);
            return memberToRemove.toMemberResponse();
        }
        // Make sure the group remain with an admin.
        if (userRoleName.equals(RoleName.ROLE_ADMIN.toString()) && userRolesService.isOnlyOneAdmin(groupId)) {
            setRandomlyNewAdmin(group.getMembersList(), groupId, userId);
        }

        group.removeMember(userId);
        groupRepository.save(group);

        return memberToRemove.toMemberResponse();
    }

    private void setRandomlyNewAdmin(List<Member> groupMembers, String groupId, UUID removedUserId) {
        Random random = new Random();
        List<Member> membersWithoutTheAdmin = groupMembers.stream().filter(member -> !member.getUserId().equals(removedUserId)).toList();
        Member randomMember = membersWithoutTheAdmin.get(random.nextInt(membersWithoutTheAdmin.size()));

        if(userRolesService.isMemberHasRole(randomMember.getUserId(),groupId)) {
            userRolesService.changeUserRole(randomMember.getUserId(),groupId,RoleName.ROLE_ADMIN.toString());
        } else {
            userRolesService.createUserRole(randomMember.getUserId(),groupId,RoleName.ROLE_ADMIN.toString());
        }
    }

    /**
     * Retrieves all groups from the database and maps them to GroupResponse DTOs.
     * @return List of GroupResponse DTOs containing details of all groups in the database.
     */
    public List<GroupResponse> getAllGroups() {
        log.info("Retrieving all groups from database...");

        return groupRepository
                .findAll()
                .stream()
                .map(Group::toGroupResponse)
                .toList();
    }

    public List<GroupResponse> getGroupsByMember(UUID memberId) {
        validator.validateGetGroupByMember(memberId);
        log.info("Retrieving groups by member from database...");

        return groupRepository
                .findAll()
                .stream()
                .filter(group -> group.isMemberInGroup(memberId))
                .map(Group::toGroupResponse)
                .toList();
    }

    /**
     * Retrieves all members of a group (Ids and usernames) from the Member and User databases, and maps them to MemberDetailResponse DTOs.
     * @param groupId ID of the group to retrieve all of its members.
     * @return List of member information.
     */
    public List<MemberDetailsResponse> getAllMembers(String groupId) {
        validator.validateGetAllMembers(groupId);

        log.info("Retrieving all members of group {} from database...", groupId);
        List<MemberDetailsResponse> result = new ArrayList<>();

        List<UUID> userIds = groupRepository.findById(groupId)
                .map(group -> group.getMembersList()
                        .stream()
                        .map(Member::getUserId).toList())
                .orElse(Collections.emptyList());

        log.info("Sending request to get member usernames from user service");
        ResponseEntity<List<UserDataResponse>> response = userClient.getGroupMembersDetails(userIds);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<UserDataResponse> members = response.getBody();
            // Fetch roles for all users
            Map<UUID, String> userRolesMap = userRolesService.getRolesForUsers(userIds, groupId);

            assert members != null;
            for (UserDataResponse member : members) {
                String roleName = userRolesMap.getOrDefault(member.userId(), RoleName.ROLE_MEMBER.toString());
                result.add(
                        new MemberDetailsResponse(member, roleName)
                );
            }
        } else {
            String errorMessage = response.hasBody() ? String.valueOf(response.getBody()) : "No error message in the response body";
            log.error("Request to user service failed. Status code {}", response.getStatusCode());
            throw new RequestFailedException("Request to user service failed. Status code " + response.getStatusCode() + "error message " + errorMessage);
        }

        return result;
    }

    /**
     * Delete a group from the database, by its ID.
     * Also delete all of its data from other services such as media-service and poll-service.
     * @param groupId ID of the group to delete.
     * @return The ID of the group that was deleted.
     */
    public String deleteGroup(String groupId) {
        validator.validateDeleteGroup(groupId);
        log.info("Deleting group with ID - '{}'", groupId);

        log.info("Sending request to delete group polls");
        ResponseEntity<DeleteGroupPollsResponse> deleteGroupPollsResponse = pollClient.deleteGroupPolls(new DeleteGroupPollsRequest(groupId));

        if (!deleteGroupPollsResponse.getStatusCode().is2xxSuccessful()) {
            log.error("Request to delete group polls failed. Status code {}", deleteGroupPollsResponse.getStatusCode());
            throw new RequestFailedException("Request to delete group polls failed. Status code " + deleteGroupPollsResponse.getStatusCode());
        }

        log.info("Sending request to delete group media");
        ResponseEntity<GroupDataDeleteResponse> groupDataDeleteResponse = mediaClient.deleteGroupData(new GroupDataDeleteRequest(groupId));

        if (!groupDataDeleteResponse.getStatusCode().is2xxSuccessful()) {
            log.error("Request to delete group media failed. Status code {}", groupDataDeleteResponse.getStatusCode());
            throw new RequestFailedException("Request to delete group data failed. Status code " + groupDataDeleteResponse.getStatusCode());
        }

        groupRepository.deleteById(groupId);
        log.info("Successfully deleted group with ID - '{}'", groupId);

        return groupId;
    }

    /**
     * Verifying the given group ID exists in the database
     * @param groupId A group ID to verify
     * @return True if the ID exists in the database. Otherwise, return false
     */
    public boolean checkGroupIdExists(String groupId) {
        log.info("Checking if group with ID - '{}' exists", groupId);
        return groupRepository.existsById(groupId);
    }

    /**
     * Fetch and return the group name by ID.
     * @param groupId The given group ID.
     * @return Information of the group.
     */
    public GroupNameResponse getGroupNameById(String groupId) {
        validator.validateGetGroupById(groupId);
        log.info("Retrieving group name with ID - '{}'", groupId);
        return groupRepository.getReferenceById(groupId).toGroupNameResponse();
    }

    /**
     * Fetch and return the group detail by ID.
     * @param groupId The given group ID.
     * @return Information of the group.
     */
    public GroupResponse getGroupById(String groupId) {
        log.info("Retrieving group with ID - '{}'", groupId);
        return groupRepository.getReferenceById(groupId).toGroupResponse();
    }

    public List<GroupResponse> getGroupsDataByIds(List<String> groupIds) {
        validator.validateMultipleIdsExist(groupIds);
        log.info("Retrieving groups' data from DB");

        return groupRepository
                .findByGroupIdIn(groupIds)
                .stream()
                .map(Group::toGroupResponse)
                .toList();
    }

    /**
     * Add a profile picture to a group's profile.
     * @param groupUpdateProfilePictureUrlRequest Information on the update requested.
     * @return DTO containing the ID of the group and the old profile picture url.
     */
    public GroupUpdateProfilePictureUrlResponse addProfilePictureUrl(GroupUpdateProfilePictureUrlRequest groupUpdateProfilePictureUrlRequest) {
        Group groupToUpdate = groupRepository
                .findById(groupUpdateProfilePictureUrlRequest.groupId())
                .orElseThrow(
                        () -> new GroupNotFoundException(groupUpdateProfilePictureUrlRequest.groupId())
                );
        String oldProfilePictureUrl = groupToUpdate.getProfilePictureUrl();

        groupToUpdate.setProfilePictureUrl(groupUpdateProfilePictureUrlRequest.profilePictureUrl());
        groupRepository.save(groupToUpdate);

        return new GroupUpdateProfilePictureUrlResponse(
                groupToUpdate.getGroupId(),
                oldProfilePictureUrl
        );
    }

    /**
     * Add a banner picture to a group's profile.
     * @param groupUpdateProfileBannerUrlRequest Information on the update requested.
     * @return DTO containing the ID of the group and the old profile banner url.
     */
    public GroupUpdateProfileBannerUrlResponse addProfileBannerUrl(GroupUpdateProfileBannerUrlRequest groupUpdateProfileBannerUrlRequest) {
        Group groupToUpdate = groupRepository
                .findById(groupUpdateProfileBannerUrlRequest.groupId())
                .orElseThrow(
                        () -> new GroupNotFoundException(groupUpdateProfileBannerUrlRequest.groupId())
                );
        String oldProfileBannerUrl = groupToUpdate.getProfilePictureUrl();

        groupToUpdate.setProfileBannerUrl(groupUpdateProfileBannerUrlRequest.profileBannerUrl());
        groupRepository.save(groupToUpdate);

        return new GroupUpdateProfileBannerUrlResponse(
                groupToUpdate.getGroupId(),
                oldProfileBannerUrl
        );
    }

    /**
     * Add a profile description to a group's profile.
     * @param groupUpdateDescriptionRequest Details on the group to update and the description to update.
     * @return ID of the updated group.
     */
    public String updateProfileDescription(GroupUpdateDescriptionRequest groupUpdateDescriptionRequest) {
        Group groupToUpdate = groupRepository
                .findById(groupUpdateDescriptionRequest.groupId())
                .orElseThrow(
                        () -> new GroupNotFoundException(groupUpdateDescriptionRequest.groupId())
                );

        groupToUpdate.setDescription(groupUpdateDescriptionRequest.description());
        groupRepository.save(groupToUpdate);

        return groupToUpdate.getGroupId();    }
}
