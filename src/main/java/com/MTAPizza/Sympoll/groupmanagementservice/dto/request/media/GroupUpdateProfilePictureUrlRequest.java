package com.MTAPizza.Sympoll.groupmanagementservice.dto.request.media;

public record GroupUpdateProfilePictureUrlRequest(
        String groupId,
        String profilePictureUrl
) {
}
