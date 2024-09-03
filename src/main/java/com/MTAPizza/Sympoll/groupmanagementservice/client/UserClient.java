package com.MTAPizza.Sympoll.groupmanagementservice.client;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service.UserIdExistsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service.UserIdResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service.UserDataResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.UUID;

public interface UserClient {
    @PostExchange("/api/user/list-by-user-ids")
    ResponseEntity<List<UserDataResponse>> getGroupMembersDetails(@RequestBody List<UUID> userIds);

    @GetExchange("/api/user/id")
    ResponseEntity<UserIdExistsResponse> checkUserIdExists(@RequestParam UUID userId);

    @GetExchange("/api/user/by-username")
    ResponseEntity<UserDataResponse> getUserDataByUsername(@RequestParam String username);

    @GetExchange("/api/user/by-user=id")
    ResponseEntity<UserDataResponse> getUserDataById(@RequestParam UUID id);
}