package com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GroupResponse (
    String groupId,
    String groupName,
    String description,
    String profilePictureUrl,
    String profileBannerUrl,
    UUID creatorId,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    LocalDateTime timeCreated,
    List<MemberResponse> membersList
) {}
