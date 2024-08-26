package com.MTAPizza.Sympoll.groupmanagementservice.service;

import com.MTAPizza.Sympoll.groupmanagementservice.client.UserClient;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.GroupResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberDetailsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MembersUsernameResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.found.ResourceNotFoundException;
import com.MTAPizza.Sympoll.groupmanagementservice.model.Group;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
import com.MTAPizza.Sympoll.groupmanagementservice.model.role.RoleName;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberService memberService;
    private final UserRolesService userRolesService;
    private final UserClient userClient;

    /**
     * Create and add a group to the database.
     * @param groupCreateRequest Details of the group to add.
     * @return Details of the group that was added to the database.
     */
    @Transactional
    public GroupResponse createGroup(GroupCreateRequest groupCreateRequest) {
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
        // TODO: validate the userID with the user service and validate userId is not already member in the group
        Member newMember = new Member(groupId, userId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group with ID " + groupId + " not found."));

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
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Could not remove member with ID " + userId + ", Group with ID " + groupId + " not found."));

        Member memberToRemove = group.getMember(userId);
        if (memberToRemove == null) {
            throw new IllegalArgumentException("Could not remove member with ID " + userId + ", member not found.");
        }

        group.removeMember(userId);
        groupRepository.save(group); // Save changes to the database

        return memberToRemove.toMemberResponse();
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
        log.info("Retrieving all members of group {} from database...", groupId);
        List<MemberDetailsResponse> result = new ArrayList<>();
        List<UUID> userIds = groupRepository.findById(groupId)
                .map(group -> group.getMembersList()
                        .stream()
                        .map(Member::getUserId).toList())
                .orElseThrow(() -> new IllegalArgumentException("Received invalid group ID - " + groupId));

        ResponseEntity<List<MembersUsernameResponse>> response = userClient.getGroupMembersDetails(userIds);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<MembersUsernameResponse> members = response.getBody();
            // Fetch roles for all users
            Map<UUID, String> userRolesMap = userRolesService.getRolesForUsers(userIds, groupId);

            for (MembersUsernameResponse member : members) {
                String roleName = userRolesMap.getOrDefault(member.userId(), "Member");
                result.add(new MemberDetailsResponse(member.userId(), member.username(), roleName));
            }
        }

        return result;
    }

    /**
     * Delete a group from the database, by its ID.
     * @param groupId ID of the group to delete.
     * @return The ID of the group that was deleted.
     */
    public String deleteGroup(String groupId) {
        log.info("Deleting group with ID - '{}'", groupId);
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Received invalid group ID - " + groupId));

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
}
