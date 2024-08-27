package com.MTAPizza.Sympoll.groupmanagementservice.service;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse createNewMember(Member member) {
        memberRepository.save(member);
        return member.toMemberResponse();
    }
}
