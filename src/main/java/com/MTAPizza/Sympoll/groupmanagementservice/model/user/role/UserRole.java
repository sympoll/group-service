package com.MTAPizza.Sympoll.groupmanagementservice.model.user.role;

import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.id.UserRoleId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@IdClass(UserRoleId.class)
public class UserRole {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Id
    @Column(name = "group_id")
    private String groupId;

    @Id
    @Column(name = "role_id")
    private int roleId;
}
