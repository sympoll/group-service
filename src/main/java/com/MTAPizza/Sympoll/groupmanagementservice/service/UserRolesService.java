package com.MTAPizza.Sympoll.groupmanagementservice.service;


import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.UserRoleDeleteResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.UserRoleResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.UserRole;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserRolesService {
    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;

    /**
     * Add a new role to a user in specific group.
     * @param userId Given user ID.
     * @param groupId Given group ID.
     * @param roleName Given role name.
     * @return A DTO object with the user id and his new role name.
     */
    public UserRoleResponse createUserRole(UUID userId, String groupId, String roleName) {
        //TODO: validation method
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
        //TODO: validation method
        log.info("Get role name for {}", userId);
        String roleName = userRoleRepository.findByUserIdAndGroupId(userId, groupId).getRoleName();
        return roleService.getRole(roleName).getRoleName();
    }

    /**
     *
     * @param userRoleChangeRequest Contain the user id, group id and the new role name.
     * @return
     */

    /**
     * Change the given user's role in specific group.
     * @param userId Given user ID.
     * @param groupId Given group ID.
     * @param newRoleName Given new role name.
     * @return The previous user's role name.
     */
    public String changeUserRole(UUID userId, String groupId, String newRoleName) {
        //TODO: validation method
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
        //TODO: validation method
        log.info("Delete user role for {}", userId);
        UserRole userRole = userRoleRepository.findByUserIdAndGroupId(userId, groupId);
        userRoleRepository.delete(userRole);
        log.info("Deleted user role for {}", userId);
        return new UserRoleDeleteResponse(userId, roleService.getRole(roleName).getRoleName());
    }
}
