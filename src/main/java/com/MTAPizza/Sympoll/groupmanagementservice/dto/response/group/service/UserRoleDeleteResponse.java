package com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service;

import java.util.UUID;

public record UserRoleDeleteResponse(
        UUID userId,
        String roleName
) {
}
