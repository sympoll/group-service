package com.MTAPizza.Sympoll.groupmanagementservice.dto.request;

import java.util.UUID;

public record GroupCreateRequest(
        String groupId,   // can be null, if so then generate a group number
        String groupName,
        String description,
        UUID creatorId
) {}