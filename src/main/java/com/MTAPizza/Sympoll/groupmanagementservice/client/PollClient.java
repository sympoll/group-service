package com.MTAPizza.Sympoll.groupmanagementservice.client;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.poll.DeleteGroupPollsRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.poll.service.DeleteGroupPollsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;

public interface PollClient {
    @DeleteExchange("/api/poll/by-group-id")
    ResponseEntity<DeleteGroupPollsResponse> deleteGroupPolls(@RequestBody DeleteGroupPollsRequest deleteGroupPollsRequest);
}
