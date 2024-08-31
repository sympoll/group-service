package com.MTAPizza.Sympoll.groupmanagementservice.service;

import com.MTAPizza.Sympoll.groupmanagementservice.client.UserClient;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.*;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.request.RequestFailedException;
import com.MTAPizza.Sympoll.groupmanagementservice.model.Group;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
import com.MTAPizza.Sympoll.groupmanagementservice.model.role.RoleName;
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
    private final UserClient userClient;
    private final Validator validator;

    /**
     * Create and add a group to the database.
     * @param groupCreateRequest Details of the group to add.
     * @return Details of the group that was added to the database.
     */
    @Transactional
    public GroupResponse createGroup(GroupCreateRequest groupCreateRequest) {
        validator.validateCreateNewGroup(groupCreateRequest);

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
        memberService.createNewMember(creator);
        createdGroup.addMember(creator);
        log.info("User with ID - '{}' added to the new group as a member", creator.getUserId());

        // Set the creator to be group admin
        userRolesService.createUserRole(createdGroup.getCreatorId(), createdGroup.getGroupId(), RoleName.ADMIN.toString());
        log.info("User with ID - '{}' set as admin in the new group", creator.getUserId());

        groupRepository.save(createdGroup);
        log.info("Group with ID - '{}' was updated with new member - '{}'", createdGroup.getGroupId(), creator.getUserId());

        return createdGroup.toGroupResponse();
    }


    /**
     * Add a new member to a group in the database.
     * @param groupId ID of the group to add the member to.
     * @param userId ID of the user to add as a member.
     * @return  Information on the member that created and added to the group.
     */
    @Transactional
    public MemberResponse addMember(String groupId, UUID userId) {
        validator.validateAddMember(groupId, userId);
        Member newMember = new Member(groupId, userId);

        Group group = groupRepository.getReferenceById(groupId);

        group.addMember(newMember);
        groupRepository.save(group); // Save changes to the database
        return memberService.createNewMember(newMember);
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
        if (userRoleName.equals(RoleName.ADMIN.toString()) && userRolesService.isOnlyOneAdmin(groupId)) {
            setRandomlyNewAdmin(group.getMembersList(), groupId, userId);
        }

        group.removeMember(userId);
        groupRepository.save(group);

        return memberToRemove.toMemberResponse();
    }

    private void setRandomlyNewAdmin(List<Member> groupMembers, String groupId, UUID removedUserId) {
        Random random = new Random();
        List<Member> membersWithoutTheAdmin = groupMembers.stream().filter(member -> member.getUserId() != removedUserId).toList();
        Member randomMember = membersWithoutTheAdmin.get(random.nextInt(membersWithoutTheAdmin.size()));

        if(userRolesService.isMemberHasRole(randomMember.getUserId(),groupId)) {
            userRolesService.createUserRole(randomMember.getUserId(),groupId,RoleName.ADMIN.toString());
        } else {
            userRolesService.changeUserRole(randomMember.getUserId(),groupId,RoleName.ADMIN.toString());
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
        ResponseEntity<List<MembersUsernameResponse>> response = userClient.getGroupMembersDetails(userIds);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<MembersUsernameResponse> members = response.getBody();
            // Fetch roles for all users
            Map<UUID, String> userRolesMap = userRolesService.getRolesForUsers(userIds, groupId);

            for (MembersUsernameResponse member : members) {
                String roleName = userRolesMap.getOrDefault(member.userId(), "Member");
                result.add(new MemberDetailsResponse(member.userId(), member.username(), roleName));
            }
        } else {
            log.error("Request to user service failed. Status code {}", response.getStatusCode());
            throw new RequestFailedException("Request to user service failed. Status code " + response.getStatusCode());
        }

        return result;
    }

    /**
     * Delete a group from the database, by its ID.
     * @param groupId ID of the group to delete.
     * @return The ID of the group that was deleted.
     */
    public String deleteGroup(String groupId) {
        validator.validateDeleteGroup(groupId);
        log.info("Deleting group with ID - '{}'", groupId);

        groupRepository.deleteById(groupId);
        log.info("Deleted group with ID - '{}'", groupId);

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
}
