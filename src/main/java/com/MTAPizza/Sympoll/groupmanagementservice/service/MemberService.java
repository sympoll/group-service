package com.MTAPizza.Sympoll.groupmanagementservice.service;

import com.MTAPizza.Sympoll.groupmanagementservice.client.UserClient;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberDetailsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.UserGroupsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.MemberRepository;
import com.MTAPizza.Sympoll.groupmanagementservice.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final Validator validator;

    public MemberResponse createNewFirstMember(Member member) {
        memberRepository.save(member);
        return member.toMemberResponse();
    }

    public MemberDetailsResponse createNewMember(Member member, String username) {
        memberRepository.save(member);
        return member.toMemberDetailsResponse(username);
    }

    public UserGroupsResponse getAllUserGroups(UUID userId) {
        validator.validateGetAllUserGroups(userId);
        return new UserGroupsResponse(memberRepository.findGroupIdsByUserId(userId));
    }
}
