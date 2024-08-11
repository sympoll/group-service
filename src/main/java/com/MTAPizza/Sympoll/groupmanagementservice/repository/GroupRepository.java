package com.MTAPizza.Sympoll.groupmanagementservice.repository;

import com.MTAPizza.Sympoll.groupmanagementservice.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
