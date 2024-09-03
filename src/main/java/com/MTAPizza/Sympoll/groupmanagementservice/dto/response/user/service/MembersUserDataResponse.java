package com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service;

import java.util.UUID;

public record MembersUserDataResponse(
        UUID userId,
        String username
) {}
