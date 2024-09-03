package com.MTAPizza.Sympoll.groupmanagementservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GroupResponse (
    String groupId,
    String groupName,
    String description,
    UUID creatorId,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    LocalDateTime timeCreated,
    List<MemberResponse> membersList
) {}
