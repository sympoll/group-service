package com.MTAPizza.Sympoll.groupmanagementservice.dto.response;

import java.util.UUID;

public record MembersUsernameResponse(
        UUID userId,
        String username
) {}
