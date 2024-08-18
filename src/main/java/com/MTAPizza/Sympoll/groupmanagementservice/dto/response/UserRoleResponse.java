package com.MTAPizza.Sympoll.groupmanagementservice.dto.response;

import java.util.UUID;

public record UserRoleResponse (
        UUID userId,
        String roleName
) {
}
