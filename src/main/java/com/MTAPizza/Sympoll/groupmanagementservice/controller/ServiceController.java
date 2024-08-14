package com.MTAPizza.Sympoll.groupmanagementservice.controller;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.GroupResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/group")
@RequiredArgsConstructor
public class ServiceController {
    private final GroupService groupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupResponse createGroup(@RequestBody GroupCreateRequest groupCreateRequest) {
        log.info("Received request to create a group");
        log.debug("Group to create received: {}", groupCreateRequest);
        return groupService.createGroup(groupCreateRequest);
    }

}
