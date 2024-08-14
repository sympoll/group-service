package com.MTAPizza.Sympoll.groupmanagementservice.dto.response;

import com.MTAPizza.Sympoll.groupmanagementservice.model.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GroupResponse (
    String groupId,
    String groupName,
    String description,
    UUID creatorId,
    LocalDateTime timeCreated,
    List<Member> membersList
) {}
