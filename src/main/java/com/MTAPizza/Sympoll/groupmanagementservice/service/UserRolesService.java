package com.MTAPizza.Sympoll.groupmanagementservice.service;


import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.UserRoleDeleteResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.UserRoleResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.role.RoleName;
import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.UserRole;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.UserRoleRepository;
import com.MTAPizza.Sympoll.groupmanagementservice.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRolesService {
    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;
    private final Validator validator;

    /**
     * Add a new role to a user in specific group.
     * @param userId Given user ID.
     * @param groupId Given group ID.
     * @param roleName Given role name.
     * @return A DTO object with the user id and his new role name.
     */
    public UserRoleResponse createUserRole(UUID userId, String groupId, String roleName) {
        validator.validateCreateUserRole(groupId, userId, roleName);
        log.info("Create user role for {}", userId);

        UserRole createdUserRole = UserRole.builder()
                .userId(userId)
                .groupId(groupId)
                .roleName(roleName)
                .build();

        userRoleRepository.save(createdUserRole);
        log.info("Created user role for {}", userId);
        return new UserRoleResponse(userId, roleService.getRole(roleName).getRoleName());
    }

    /**
     * Return the given user's role name in specific group.
     * @param userId Given user ID.
     * @param groupId Given group ID.
     * @return The name of the user's role in the given group.
     */
    public String getRoleNameOfSpecificUser(UUID userId, String groupId) {
        String result = "";

        if(userRoleRepository.existsByUserIdAndGroupId(userId, groupId)) {
            log.info("Get role name for {}", userId);
            result = userRoleRepository.findByUserIdAndGroupId(userId, groupId).getRoleName();
        }

        return result;
    }

    /**
     * Fetch roles for each given member of the group.
     * @param userIds List of the group members IDs.
     * @param groupId The given group ID.
     * @return Map of member ID and the member's role in the group.
     */
    public Map<UUID, String> getRolesForUsers(List<UUID> userIds, String groupId) {
        validator.validateGetRolesForUsers(userIds, groupId);
        List<UserRole> userRoles = userRoleRepository.findByUserIdInAndGroupId(userIds, groupId);
        return userRoles.stream()
                .collect(Collectors.toMap(UserRole::getUserId, UserRole::getRoleName));
    }

    /**
     * Change the given user's role in specific group.
     * @param userId Given user ID.
     * @param groupId Given group ID.
     * @param newRoleName Given new role name.
     * @return The previous user's role name.
     */
    public String changeUserRole(UUID userId, String groupId, String newRoleName) {
        validator.validateChangeUserRole(groupId, userId, newRoleName);
        String previousRoleName;

        log.info("Change user role for {}", userId);
        UserRole userRole = userRoleRepository.findByUserIdAndGroupId(userId, groupId);
        previousRoleName = userRole.getRoleName();
        userRole.setRoleName(newRoleName);
        userRoleRepository.save(userRole);
        log.info("Changed user role for {}", userId);
        return previousRoleName;
    }

    /**
     * Delete a user role from the database.
     * @param userId Given user ID.
     * @param groupId Given group ID.
     * @param roleName Given role name.
     * @return A DTO with the user id and his deleted role name.
     */
    public UserRoleDeleteResponse deleteUserRole(UUID userId, String groupId, String roleName) {
        validator.validateDeleteUserRole(groupId, userId, roleName);
        log.info("Delete user role for {}", userId);
        UserRole userRole = userRoleRepository.findByUserIdAndGroupId(userId, groupId);
        userRoleRepository.delete(userRole);
        log.info("Deleted user role for {}", userId);
        return new UserRoleDeleteResponse(userId, roleService.getRole(roleName).getRoleName());
    }

    /**
     * Verifying the given user's permission to delete a poll in the group.
     * @param userId Given user ID
     * @param groupId Given group ID
     * @return True value if the user has permission to delete polls in the given group. Otherwise, return false.
     */
    public boolean hasPermissionToDeletePoll(UUID userId, String groupId) {
        validator.validateHasPermissionToDeletePoll(groupId, userId);
        log.info("Check if user {} has permission to delete polls", userId);
        UserRole userRole = userRoleRepository.findByUserIdAndGroupId(userId, groupId);
        boolean result = false;

        if(userRole.getRoleName().equals(RoleName.MODERATOR.toString()) || userRole.getRoleName().equals(RoleName.ADMIN.toString())){
            result = true;
        }else {
            //TODO: handle costume role permissions verification.
        }

        return result;
    }

    public boolean isOnlyOneAdmin(String groupId) {
        log.info("Check if there is only one admin in the group");
        List<UserRole> groupRoles = userRoleRepository.findByGroupId(groupId);

        long adminsCount = groupRoles.stream()
                .filter(userRole -> userRole.getRoleName().equals(RoleName.ADMIN.toString()))
                .count();

        return adminsCount == 1;
    }

    public boolean isMemberHasRole(UUID userId, String groupId) {
        log.info("Check if user {} has role in group {}", userId, groupId);
        return userRoleRepository.existsByUserIdAndGroupId(userId, groupId);
    }
}
