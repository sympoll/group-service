package com.MTAPizza.Sympoll.groupmanagementservice.repository;

import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.UserRole;
import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    UserRole findByUserIdAndGroupId(UUID userId, String groupId);
}
