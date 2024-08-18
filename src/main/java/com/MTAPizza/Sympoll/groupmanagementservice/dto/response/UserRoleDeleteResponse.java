package com.MTAPizza.Sympoll.groupmanagementservice.dto.response;

import java.util.UUID;

public record UserRoleDeleteResponse(
        UUID userId,
        String roleName
) {
}
