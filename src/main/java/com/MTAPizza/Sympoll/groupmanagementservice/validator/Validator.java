package com.MTAPizza.Sympoll.groupmanagementservice.validator;

import com.MTAPizza.Sympoll.groupmanagementservice.client.UserClient;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.request.GroupCreateRequest;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.UserIdExistsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.found.ResourceNotFoundException;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.member.UserAlreadyMemberException;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.member.UserNotMemberException;
import com.MTAPizza.Sympoll.groupmanagementservice.exception.request.RequestFailedException;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.GroupRepository;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.MemberRepository;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class Validator {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final UserClient userClient;

    public void validateCreateNewGroup(GroupCreateRequest groupCreateRequest) {
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

    public void validateCreateUserRole(String groupId, UUID userId, String roleName) {
        validateGetGroupById(groupId);
        validateUserId(userId);
        validateRoleNameExists(roleName);
    }

    public void  validateGetRoleNameOfSpecificUser (UUID userId, String groupId) {
        validateGroupIdExists(groupId);
        validateUserId(userId);
    }

    public void validateGetRolesForUsers(List<UUID> userIds, String groupId) {
        validateUserIdsList(userIds);
        validateGroupIdExists(groupId);
    }

    public void validateChangeUserRole(String groupId, UUID userId, String newRoleName) {
        validateGroupIdExists(groupId);
        validateUserId(userId);
        validateRoleNameExists(newRoleName);
    }

    public void validateDeleteUserRole(String groupId, UUID userId, String newRoleName) {
        validateGroupIdExists(groupId);
        validateUserId(userId);
        validateRoleNameExists(newRoleName);
    }

    public void validateHasPermissionToDeletePoll(String groupId, UUID userId) {
        validateGroupIdExists(groupId);
        validateUserId(userId);
    }

    private void validateUserId(UUID userId) {
        log.info("Sending validating request to user service");
        ResponseEntity<UserIdExistsResponse> response = userClient.checkUserIdExists(userId);

        if (response.getStatusCode().is2xxSuccessful()) {
            if (!response.getBody().isExists()) {
                log.info("User {} does not exist", userId);
                throw new ResourceNotFoundException("User " + userId + " does not exist");
            }
        } else {
            log.error("Request to user service with user id '{}' failed. Status code {}", userId, response.getStatusCode());
            throw new RequestFailedException("Request to user service failed. Status code " + response.getStatusCode());
        }
    }

    private void validateGroupIdExists(String groupId) {
        if(!groupRepository.existsById(groupId)) {
            log.info("Group {} does not exist", groupId);
            throw new ResourceNotFoundException("Group " + groupId + " does not exist");
        }
    }

    private void validateUserAlreadyMember(String groupId, UUID userId) {
        if(memberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            log.info("User {} already member of group {}", userId, groupId);
            throw new UserAlreadyMemberException("User " + userId + " already member of group " + groupId);
        }
    }

    private void validateUserIsGroupMember(String groupId, UUID userId) {
        if(!memberRepository.existsByGroupIdAndUserId(groupId, userId)) {
            log.info("User {} is not a member of group {}", userId, groupId);
            throw new UserNotMemberException("User " + userId + " is not a member of group " + groupId);
        }
    }

    private void validateRoleNameExists(String roleName) {
        if(!roleRepository.existsById(roleName)) {
            log.info("Role {} does not exist", roleName);
            throw new ResourceNotFoundException("Role " + roleName + " does not exist");
        }
    }

    private void validateUserIdsList(List<UUID> userIds) {
        for(UUID id : userIds) {
            validateUserId(id);
        }
    }
}
