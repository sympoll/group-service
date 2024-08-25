package com.MTAPizza.Sympoll.groupmanagementservice.model.role;

import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.UserRole;

public enum RoleName {
    ADMIN,
    MODERATOR;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}

