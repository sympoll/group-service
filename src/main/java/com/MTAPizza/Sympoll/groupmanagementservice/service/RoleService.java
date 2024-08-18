package com.MTAPizza.Sympoll.groupmanagementservice.service;

import com.MTAPizza.Sympoll.groupmanagementservice.model.role.Role;
import com.MTAPizza.Sympoll.groupmanagementservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getRole(UUID roleId) {
        // TODO: validator methods
        log.info("Getting role with id {}", roleId);
        return roleRepository.getReferenceById(roleId);
    }

}
