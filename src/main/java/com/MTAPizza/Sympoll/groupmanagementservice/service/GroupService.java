package com.MTAPizza.Sympoll.groupmanagementservice.service;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.GroupResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.Group;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupResponse createGroup(GroupCreateRequest groupCreateRequest) {
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
}
