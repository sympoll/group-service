package com.MTAPizza.Sympoll.groupmanagementservice.service;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.GroupResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.Group;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupResponse createGroup(GroupCreateRequest groupCreateRequest) {
        // TODO: Validate request

        // TODO: Add creator Id into a new list of admins
        Group createdGroup = Group.builder()
                .groupId(groupCreateRequest.groupId())
                .groupName(groupCreateRequest.groupName())
                .description(groupCreateRequest.description())
                .creatorId(groupCreateRequest.creatorId())
                .build();

        groupRepository.save(createdGroup);
        log.info("Group with ID - '{}' was created by - '{}'", createdGroup.getGroupId(), createdGroup.getCreatorId());

        return createdGroup.toGroupResponse();
    }
}
