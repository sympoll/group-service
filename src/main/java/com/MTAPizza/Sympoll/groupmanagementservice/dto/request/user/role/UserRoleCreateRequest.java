package com.MTAPizza.Sympoll.groupmanagementservice.dto.request.user.role;

import java.util.UUID;

public record UserRoleCreateRequest(
        UUID userId,
        String groupId,
        String roleName
) {
}
