package com.MTAPizza.Sympoll.groupmanagementservice.repository;

import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.id.MemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, MemberId> {
    boolean existsByGroupIdAndUserId(String groupId, UUID userId);

    @Query("SELECT m.groupId FROM Member m WHERE m.userId = :userId")
    List<String> findGroupIdsByUserId(UUID userId);}
