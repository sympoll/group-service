package com.MTAPizza.Sympoll.groupmanagementservice.dto.response;

import java.util.List;
import java.util.UUID;

public record DeleteGroupPollsResponse(List<UUID> pollsIds) {
}
