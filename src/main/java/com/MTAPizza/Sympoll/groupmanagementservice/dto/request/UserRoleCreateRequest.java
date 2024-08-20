package com.MTAPizza.Sympoll.groupmanagementservice.dto.request;

import java.util.UUID;

public record UserRoleCreateRequest(
        UUID userId,
        String groupId,
        int roleId
) {
}
