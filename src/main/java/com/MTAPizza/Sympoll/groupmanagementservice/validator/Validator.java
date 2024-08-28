package com.MTAPizza.Sympoll.groupmanagementservice.validator;

import com.MTAPizza.Sympoll.groupmanagementservice.client.UserClient;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.UserIdExistsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.found.GroupNotFoundException;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.found.UserNotFoundException;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.member.UserAlreadyMemberException;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.member.UserNotMemberException;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.id.MemberId;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.GroupRepository;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.MemberRepository;
import com.MTAPizza.Sympoll.groupmanagementservice.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Validator {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final UserClient userClient;

    public void validateNewGroup(GroupCreateRequest groupCreateRequest) {
        validateUserId(groupCreateRequest.creatorId());
    }

    public void validateAddMember(String groupId, UUID userId) {
        validateGroupIdExists(groupId);
        validateUserId(userId);
        validateUserAlreadyMember(groupId, userId);

    }

    public void validateRemoveMember(String groupId, UUID userId) {
        validateGroupIdExists(groupId);
        validateUserId(userId);
        validateUserIsGroupMember(groupId, userId);
    }

    public void validateGetGroupByMember(UUID memberId) {
        validateUserId(memberId);
    }

    public void  validateGetAllMembers(String groupId) {
        validateGroupIdExists(groupId);
    }

    public void validateDeleteGroup(String groupId) {
        validateGroupIdExists(groupId);
    }

    public void validateGetGroupById(String groupId) {
        validateGroupIdExists(groupId);
    }

    private void validateUserId(UUID userId) {
        ResponseEntity<UserIdExistsResponse> response = userClient.checkUserIdExists(userId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            if (!response.getBody().isExists()) {
                log.warn("User {} does not exist", userId);
                throw new UserNotFoundException("User " + userId + " does not exist");
            }
        }
    }

    private void validateGroupIdExists(String groupId) {
        if(!groupRepository.existsById(groupId)) {
            log.warn("Group {} does not exist", groupId);
            throw new GroupNotFoundException("Group " + groupId + " does not exist");
        }
    }

    private void validateUserAlreadyMember(String groupId, UUID userId) {
        if(!memberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            log.warn("User {} already member of group {}", userId, groupId);
            throw new UserAlreadyMemberException("User " + userId + " already member of group " + groupId);
        }
    }

    private void validateUserIsGroupMember(String groupId, UUID userId) {
        if(!memberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            log.warn("User {} is not a member of group {}", userId, groupId);
            throw new UserNotMemberException("User " + userId + " is not a member of group " + groupId);
        }
    }
}
