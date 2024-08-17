package com.MTAPizza.Sympoll.groupmanagementservice.service;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.GroupResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.Group;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    /**
     * Create and add a group to the database.
     * @param groupCreateRequest Details of the group to add.
     * @return Details of the group that was added to the database.
     */
    @Transactional
    public GroupResponse createGroup(GroupCreateRequest groupCreateRequest) {
        log.info("Creating group {}", groupCreateRequest);

        // TODO: Validate request, including checking the the group ID received (if any) is not already in the DB.
        String groupIdReceived = groupCreateRequest.groupId();

        // TODO: Add creator Id into a new list of admins
        Group createdGroup = Group.builder()
                .groupId(groupIdReceived != null ? groupIdReceived : UUID.randomUUID().toString().replaceAll("[^0-9]", "")) // If defined a group ID then use it, otherwise generate random group ID.
                .groupName(groupCreateRequest.groupName())
                .description(groupCreateRequest.description())
                .creatorId(groupCreateRequest.creatorId())
                .build();

        groupRepository.save(createdGroup);
        log.info("Group with ID - '{}' was created by - '{}'", createdGroup.getGroupId(), createdGroup.getCreatorId());

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
        // TODO: validate the userID with the user service
        Member newMember = new Member(groupId, userId);

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group with ID " + groupId + " not found."));

        group.addMember(newMember);
        groupRepository.save(group); // Save changes to the database
        return newMember.toMemberResponse();
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

    /**
     * Retrieves all members of a group from the database and maps them to MemberResponse DTOs.
     * @param groupId ID of the group to retrieve all of its members.
     * @return List of member information.
     */
    public List<MemberResponse> getAllMembers(String groupId) {
        log.info("Retrieving all members of group {} from database...", groupId);
        return groupRepository.findById(groupId)
                .map(group -> group.getMembersList()
                        .stream()
                        .map(Member::toMemberResponse)
                        .toList()
                )
                .orElseThrow(() -> new IllegalArgumentException("Received invalid group ID - " + groupId));
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

    public boolean checkGroupIdExists(String groupId) {
        log.info("Checking if group with ID - '{}' exists", groupId);
        return groupRepository.existsById(groupId);
    }
}
