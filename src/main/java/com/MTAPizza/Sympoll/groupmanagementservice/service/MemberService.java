package com.MTAPizza.Sympoll.groupmanagementservice.service;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service.MemberDetailsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service.UserGroupsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service.UserDataResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.MemberRepository;
import com.MTAPizza.Sympoll.groupmanagementservice.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final Validator validator;

    public MemberDetailsResponse createNewMember(Member member, UserDataResponse userDataResponse, String userRole) {
        memberRepository.save(member);
        return member.toMemberDetailsResponse(userDataResponse, userRole);
    }

    public UserGroupsResponse getAllUserGroups(UUID userId) {
        validator.validateGetAllUserGroups(userId);
        return new UserGroupsResponse(memberRepository.findGroupIdsByUserId(userId));
    }
}
