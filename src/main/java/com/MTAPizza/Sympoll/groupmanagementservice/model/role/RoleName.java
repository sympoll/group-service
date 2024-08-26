package com.MTAPizza.Sympoll.groupmanagementservice.model.role;

import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.UserRole;

public enum RoleName {
    ADMIN,
    MODERATOR;

    @Override
    public String toString() {
        String name = this.name().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}

