package com.MTAPizza.Sympoll.groupmanagementservice.repository;

import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.id.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, MemberId> {
}
