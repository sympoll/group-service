package com.MTAPizza.Sympoll.groupmanagementservice.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Data
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String groupId;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by_user")
    private UUID creatorId;

    @Column(name = "time_created")
    private final LocalDateTime timeCreated = LocalDateTime.now(); // Initialize to the current time.

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "group_id")
    private List<Member> membersList;
}
