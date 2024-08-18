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
    private UserRoleRepository userRoleRepository;
    private RoleService roleService;

    public UserRoleResponse createUserRole(UUID userId, String groupId, UUID roleId) {
        //TODO: validation method
        log.info("Create user role for {}", userId);

        UserRole createdUserRole = UserRole.builder()
                .userId(userId)
                .groupId(groupId)
                .roleId(roleId)
                .build();

        userRoleRepository.save(createdUserRole);
        log.info("Created user role for {}", userId);
        return new UserRoleResponse(userId, roleService.getRole(roleId).getRoleName());
    }

    public UUID getRoleIdOfSpecificUser(UUID userId, String groupId) {
        //TODO: validation method
        log.info("Get role id for {}", userId);
        return userRoleRepository.findByUserIdAndGroupId(userId,groupId).getRoleId();
    }

    public String getRoleNameOfSpecificUser(UUID userId, String groupId) {
        //TODO: validation method
        log.info("Get role name for {}", userId);
        UUID roleId = userRoleRepository.findByUserIdAndGroupId(userId, groupId).getRoleId();
        return roleService.getRole(roleId).getRoleName();
    }

    public UserRoleDeleteResponse deleteUserRole(UUID userId, String groupId, UUID roleId) {
        //TODO: validation method
        log.info("Delete user role for {}", userId);
        UserRole userRole = userRoleRepository.findByUserIdAndGroupId(userId, groupId);
        userRoleRepository.delete(userRole);
        log.info("Deleted user role for {}", userId);
        return new UserRoleDeleteResponse(userId, roleService.getRole(roleId).getRoleName());
    }


}
