package com.MTAPizza.Sympoll.groupmanagementservice.dto.response;

import java.util.UUID;

public record MemberDetailsResponse(
        UUID userId,
        String username,
        String roleName
) {
}
