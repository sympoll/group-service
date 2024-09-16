package com.MTAPizza.Sympoll.groupmanagementservice.client;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media.request.delete.GroupDataDeleteRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.media.service.GroupDataDeleteResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;

public interface MediaClient {
    @DeleteExchange("/api/media/group/data")
    ResponseEntity<GroupDataDeleteResponse> deleteGroupData(@RequestBody GroupDataDeleteRequest groupDataDeleteRequest);
}