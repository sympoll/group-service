package com.MTAPizza.Sympoll.groupmanagementservice.dto.request.user.role;

import java.util.UUID;

public record UserRoleChangeRequest(
        UUID userId,
        String groupId,
        String newRoleName
) {
}
