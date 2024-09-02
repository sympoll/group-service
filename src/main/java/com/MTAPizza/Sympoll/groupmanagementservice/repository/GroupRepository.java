package com.MTAPizza.Sympoll.groupmanagementservice.repository;

import com.MTAPizza.Sympoll.groupmanagementservice.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
    List<Group> findByGroupIdIn(List<String> groupIds);
}
