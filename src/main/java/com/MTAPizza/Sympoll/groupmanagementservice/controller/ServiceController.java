package com.MTAPizza.Sympoll.groupmanagementservice.controller;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.DeleteGroupResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.GroupResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/group")
@RequiredArgsConstructor
public class ServiceController {
    private final GroupService groupService;

    /**
     * Add a new group to the database.
     * @param groupCreateRequest Details of the group to create.
     * @return Full information of the group that was created.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponse createGroup(@RequestBody GroupCreateRequest groupCreateRequest) {
        log.info("Received request to create a group");
        log.debug("Group to create received: {}", groupCreateRequest);
        return groupService.createGroup(groupCreateRequest);
    }

    /**
     * Add a user as a new member to a group.
     * @param groupId ID of the group to add the user to.
     * @param userId ID of the user to add to the group.
     * @return Information of the member that was created and added to the group.
     */
    @PostMapping("/add-member")
    @ResponseStatus(HttpStatus.OK)
    public MemberResponse addMember(@RequestParam String groupId, @RequestParam UUID userId) {
        log.info("Received request to add the member {} to the group, {}", userId, groupId);
        return groupService.addMember(groupId, userId);
    }

    /**
     * Fetch and return all groups from the database.
     * @return List of group information.
     */
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<GroupResponse> getAllGroups() {
        log.info("Received request to get all groups");
        return groupService.getAllGroups();
    }

    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public List<MemberResponse> getAllMembers(String groupId) {
        log.info("Received request to get all members of group {}", groupId);
        return groupService.getAllMembers(groupId);
    }

    /**
     * Delete a group from the database, by its ID.
     * @param groupId ID of the group to delete.
     * @return ID of the group that was deleted.
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public DeleteGroupResponse deleteGroup(@RequestParam String groupId) {
        log.info("Received a request to delete group with id {}", groupId);
        return new DeleteGroupResponse(groupService.deleteGroup(groupId));
    }
}
