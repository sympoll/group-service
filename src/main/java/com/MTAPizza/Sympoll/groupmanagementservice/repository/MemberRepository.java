package com.MTAPizza.Sympoll.groupmanagementservice.repository;

import com.MTAPizza.Sympoll.groupmanagementservice.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
