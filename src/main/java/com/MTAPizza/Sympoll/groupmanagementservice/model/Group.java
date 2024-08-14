package com.MTAPizza.Sympoll.groupmanagementservice.model;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.GroupResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "groups")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    private String groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "description")
    private String description;

    @Column(name = "creator_id")
    private UUID creatorId;

    @Column(name = "time_created")
    private final LocalDateTime timeCreated = LocalDateTime.now();  // Initialize to the current time.

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "group_id")
    private List<Member> membersList = new ArrayList<>();           // Initialize to an empty members list.

    // TODO: Add Admins list, will be initialized with the creatorId as the only admin.

    public GroupResponse toGroupResponse() {
        return new GroupResponse(
                groupId,
                groupName,
                description,
                creatorId,
                timeCreated,
                membersList.stream().map(Member::toMemberResponse).toList() // Convert to member response
        );
    }
}
