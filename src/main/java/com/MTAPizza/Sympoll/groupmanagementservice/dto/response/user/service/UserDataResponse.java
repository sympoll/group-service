package com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service;

import java.util.UUID;

public record UserDataResponse (
        UUID userId,
        String username,
        String email,
        String profilePictureUrl
) {
}
