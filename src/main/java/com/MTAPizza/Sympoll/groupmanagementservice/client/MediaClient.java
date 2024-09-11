package com.MTAPizza.Sympoll.groupmanagementservice.client;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.GroupDataDeleteRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.media.service.GroupDataDeleteResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service.UserDataResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service.UserIdExistsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.UUID;

public interface MediaClient {
    @DeleteExchange("/api/media/group/data")
    ResponseEntity<GroupDataDeleteResponse> deleteGroupData(@RequestBody GroupDataDeleteRequest groupDataDeleteRequest);
}