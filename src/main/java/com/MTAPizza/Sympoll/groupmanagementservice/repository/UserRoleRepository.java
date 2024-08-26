package com.MTAPizza.Sympoll.groupmanagementservice.repository;

import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.UserRole;
import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    UserRole findByUserIdAndGroupId(UUID userId, String groupId);
    boolean isExistsByUserIdAndGroupId(UUID userId, String groupId);
    List<UserRole> findByUserIdInAndGroupId(List<UUID> userIds, String groupId);
}
