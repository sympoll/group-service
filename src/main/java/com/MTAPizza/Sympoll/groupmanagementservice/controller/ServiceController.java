package com.MTAPizza.Sympoll.groupmanagementservice.controller;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.decription.GroupUpdateDescriptionRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.response.update.GroupUpdateProfileBannerUrlResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.response.update.GroupUpdateProfilePictureUrlResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.user.role.UserRoleChangeRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.user.role.UserRoleCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.user.role.UserRoleDeleteRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.request.update.GroupUpdateProfileBannerUrlRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.request.update.GroupUpdateProfilePictureUrlRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service.DeleteGroupResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service.*;
import com.MTAPizza.Sympoll.groupmanagementservice.service.GroupService;
import com.MTAPizza.Sympoll.groupmanagementservice.service.MemberService;
import com.MTAPizza.Sympoll.groupmanagementservice.service.UserRolesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/group")
@RequiredArgsConstructor
public class
ServiceController {
    private final GroupService groupService;
    private final UserRolesService userRolesService;
    private final MemberService memberService;

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
     * @param username The user's username to add to the group.
     * @return Information of the member that was created and added to the group.
     */
    @PostMapping("/add-member")
    @ResponseStatus(HttpStatus.OK)
    public MemberDetailsResponse addMember(@RequestParam String groupId, @RequestParam String username) {
        log.info("Received request to add the member {} to the group, {}", username, groupId);
        return groupService.addMember(groupId, username);
    }

    /**
     * Remove a user from the members list of a group.
     * @param groupId ID of the group to remove the user from.
     * @param userId ID of the user to remove from the group.
     * @return Information of the member that was removed from the group.
     */
    @DeleteMapping("/remove-member")
    @ResponseStatus(HttpStatus.OK)
    public MemberResponse removeMember(@RequestParam String groupId, @RequestParam UUID userId) {
        log.info("Received request to remove the member {} from the group, {}", userId, groupId);
        return groupService.removeMember(groupId, userId);
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

    @GetMapping("/by-member-id")
    @ResponseStatus(HttpStatus.OK)
    public List<GroupResponse> getGroupsByMemberId(@RequestParam UUID memberId) {
        log.info("Received request to get groups by member id {}", memberId);
        return groupService.getGroupsByMember(memberId);
    }

    @GetMapping("/members")
    @ResponseStatus(HttpStatus.OK)
    public List<MemberDetailsResponse> getAllMembers(@RequestParam String groupId) {
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

    /**
     * Verifying the given group ID exists in the database.
     * @param groupId A group ID to verify.
     * @return A DTO with 'isExists' boolean value.
     */
    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    public GroupIdExistsResponse checkGroupIdExists(@RequestParam String groupId) {;
        log.info("Received a request to check group id exists with id {}", groupId);
        return new GroupIdExistsResponse(groupService.checkGroupIdExists(groupId));
    }

    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public String HealthCheck(){
        log.info("Received a request to health check");
        return "OK";
    }

    /**
     * Add a new role to a user in specific group.
     * @param userRoleCreateRequest Contain the user id, group id and role name.
     * @return A DTO object with the user id and his new role name.
     */
    @PostMapping("/user-role")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRoleResponse createUserRole(@RequestBody UserRoleCreateRequest userRoleCreateRequest) {
        log.info("Received a request to create a user role");
        return userRolesService.createUserRole(userRoleCreateRequest.userId(), userRoleCreateRequest.groupId(), userRoleCreateRequest.roleName());
    }

    /**
     * Return the given user's role name in specific group.
     * @param userId Given user ID.
     * @param groupId Given group ID.
     * @return The name of the user's role in the given group.
     */
    @GetMapping("/user-role")
    @ResponseStatus(HttpStatus.OK)
    public String getRoleNameOfSpecificUser(@RequestParam UUID userId, @RequestParam String groupId) {
        log.info("Received a request to get role name of specific user");
        return userRolesService.getRoleNameOfSpecificUser(userId, groupId);
    }

    /**
     * Change the given user's role in specific group.
     * @param userRoleChangeRequest Contain the user id, group id and the new role name.
     * @return The previous user's role name.
     */
    @PutMapping("/user-role")
    @ResponseStatus(HttpStatus.OK)
    public String changeUserRole(@RequestBody UserRoleChangeRequest userRoleChangeRequest) {
        log.info("Received a request to change user role");
        return userRolesService.changeUserRole(userRoleChangeRequest.userId(), userRoleChangeRequest.groupId(), userRoleChangeRequest.newRoleName());
    }

    /**
     * Delete a user role from the database.
     * @param userRoleDeleteRequest Contain the user id, group id and role name.
     * @return A DTO with the user id and his deleted role name.
     */
    @DeleteMapping("/user-role")
    @ResponseStatus(HttpStatus.OK)
    public UserRoleDeleteResponse deleteUserRole(@RequestBody UserRoleDeleteRequest userRoleDeleteRequest) {
        log.info("Received a request to delete user role");
        return userRolesService.deleteUserRole(userRoleDeleteRequest.userId(), userRoleDeleteRequest.groupId(), userRoleDeleteRequest.roleName());
    }

    /**
     * Verifying the given user's permission to delete a poll in the group.
     * @param userId Given user ID
     * @param groupId Given group ID
     * @return True value if the user has permission to delete polls in the given group. Otherwise, return false.
     */
    @GetMapping("/user-role/permission/delete")
    @ResponseStatus(HttpStatus.OK)
    public boolean checkUserPermissionToDeletePoll(@RequestParam UUID userId, @RequestParam String groupId) {
        log.info("Received a request to check user permission to delete poll");
        return userRolesService.hasPermissionToDeletePoll(userId, groupId);
    }

    /**
     * Fetch and return the group name by ID.
     * @param groupId The given group ID.
     * @return Information of the group.
     */
    @GetMapping("/name/by-group-id")
    @ResponseStatus(HttpStatus.OK)
    public GroupNameResponse getGroupNameById(@RequestParam String groupId) {
        log.info("Received a request to get group name by id {}", groupId);
        return groupService.getGroupNameById(groupId);
    }

    /**
     * Fetch and return the group details by ID.
     * @param groupId The given group ID.
     * @return Information of the group.
     */
    @GetMapping("/by-group-id")
    @ResponseStatus(HttpStatus.OK)
    public GroupResponse getGroupById(@RequestParam String groupId) {
        log.info("Received a request to get group by id {}", groupId);
        return groupService.getGroupById(groupId);
    }

    @GetMapping("/all-user-groups")
    @ResponseStatus(HttpStatus.OK)
    public UserGroupsResponse getAllUserGroups(@RequestParam UUID userId) {
        log.info("Received request to get all group ids of use {}", userId);
        return memberService.getAllUserGroups(userId);
    }

    /**
     * Fetch and retrieve a list of groups' data by their IDs.
     * @param groupIds Given group IDs.
     * @return List of group data DTOs.
     */
    @PostMapping("/groups-list")
    @ResponseStatus(HttpStatus.OK)
    public List<GroupResponse> getGroupsDataByIds(@RequestBody List<String> groupIds){
        log.info("Received request to retrieve groups' data by IDs");
        return groupService.getGroupsDataByIds(groupIds);
    }

    /**
     * Save a profile picture for a group
     * @param groupUpdateProfilePictureUrlRequest Information on the update to perform.
     * @return The updated group's ID.
     */
    @PostMapping(value = "/profile-picture-url", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GroupUpdateProfilePictureUrlResponse updateProfilePictureUrl(@RequestBody GroupUpdateProfilePictureUrlRequest groupUpdateProfilePictureUrlRequest){
        log.info("Received request to save a profile picture url");
        log.debug("Request received to add profile picture url: {}", groupUpdateProfilePictureUrlRequest);
        return groupService.addProfilePictureUrl(groupUpdateProfilePictureUrlRequest);
    }

    /**
     * Save a banner picture for a group
     * @param groupUpdateProfileBannerUrlRequest Information on the update to perform.
     * @return The updated group's ID.
     */
    @PostMapping(value = "/profile-banner-url", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public GroupUpdateProfileBannerUrlResponse updateProfileBannerUrl(@RequestBody GroupUpdateProfileBannerUrlRequest groupUpdateProfileBannerUrlRequest){
        log.info("Received request to save a banner picture url");
        log.debug("Request received to add banner picture url: {}", groupUpdateProfileBannerUrlRequest);
        return groupService.addProfileBannerUrl(groupUpdateProfileBannerUrlRequest);
    }


    /**
     * Save a group profile description.
     * @param groupUpdateDescriptionRequest Information on the group and the description to save.
     * @return the updated group's ID.
     */
    @PostMapping("/description")
    @ResponseStatus(HttpStatus.OK)
    public String updateProfileDescription(@RequestBody GroupUpdateDescriptionRequest groupUpdateDescriptionRequest){
        log.info("Received request to save a description");
        log.debug("Request received to add description: {}", groupUpdateDescriptionRequest);
        return groupService.updateProfileDescription(groupUpdateDescriptionRequest);
    }
}
