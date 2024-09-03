package com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service.UserDataResponse;

public record MemberDetailsResponse(
        UserDataResponse userData,
        String roleName
) {
}
