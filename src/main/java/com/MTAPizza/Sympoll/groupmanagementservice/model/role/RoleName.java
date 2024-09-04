package com.MTAPizza.Sympoll.groupmanagementservice.model.role;

import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.UserRole;

public enum RoleName {
    ROLE_ADMIN,
    ROLE_MODERATOR,
    ROLE_MEMBER;

    @Override
    public String toString() {
        return switch (this) {
            case ROLE_ADMIN -> "Admin";
            case ROLE_MODERATOR -> "Moderator";
            case ROLE_MEMBER -> "Member";
        };
    }
}

