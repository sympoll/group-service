package com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.id;

import lombok.Data;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
public class UserRoleId implements Serializable {
    private String groupId;

    private UUID userId;

    private String roleName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(userId, that.userId) && Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId, roleName);
    }
}
