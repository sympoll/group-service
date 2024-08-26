package com.MTAPizza.Sympoll.groupmanagementservice.client;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberDetailsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.UUID;

public interface UserClient {
    @GetExchange("/api/user/username-list")
    ResponseEntity<List<MemberDetailsResponse>> getGroupMembersDetails(@RequestParam List<UUID> userIds);
}