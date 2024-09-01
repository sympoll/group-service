package com.MTAPizza.Sympoll.groupmanagementservice.client;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MembersUsernameResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.UserIdExistsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.UserIdResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.UUID;

public interface UserClient {
    @PostExchange("/api/user/username-list")
    ResponseEntity<List<MembersUsernameResponse>> getGroupMembersDetails(@RequestBody List<UUID> userIds);

    @GetExchange("/api/user/id")
    ResponseEntity<UserIdExistsResponse> checkUserIdExists(@RequestParam UUID userId);

    @GetExchange("/api/user/by-username")
    ResponseEntity<UserIdResponse> getUserIdByUsername(@RequestParam String username);
}