package com.MTAPizza.Sympoll.groupmanagementservice.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GroupResponse (
    String groupId,
    String groupName,
    String description,
    UUID creatorId,
    LocalDateTime timeCreated,
    List<MemberResponse> membersList
) {}
