package com.MTAPizza.Sympoll.groupmanagementservice.dto.request;

import java.util.UUID;

public record UserRoleChangeRequest(
        UUID userId,
        String groupId,
        String newRoleName
) {
}
