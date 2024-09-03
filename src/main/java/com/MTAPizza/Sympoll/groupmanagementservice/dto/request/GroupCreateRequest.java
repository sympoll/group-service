package com.MTAPizza.Sympoll.groupmanagementservice.dto.request;

import java.util.UUID;

public record GroupCreateRequest(
        String groupName,
        String description,
        UUID userId
) {}