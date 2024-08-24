package com.MTAPizza.Sympoll.groupmanagementservice.model;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.GroupResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
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

    @OneToMany(mappedBy = "groupId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Member> membersList = new ArrayList<>();          // Initialize to an empty members list.


    /**
     * Add a new member to the group.
     * @param member Member to add to the group.
     */
    public void addMember(Member member) {
        if (!member.getGroupId().equals(groupId))
            throw new IllegalArgumentException("Invalid member received for group " + groupId + ", member's group ID is " + member.getGroupId());
        if(membersList == null){
            membersList = new ArrayList<>();
        }
        membersList.add(member);
    }

    /**
     * Remove a member from the group.
     * @param memberId ID of the member to remove from the group.
     */
    public void removeMember(UUID memberId) {
        membersList.removeIf((member) -> member.getUserId().equals(memberId));
    }

    /**
     * Find and return a member in the group's members list.
     * @param memberId ID of the member.
     * @return Member object.
     */
    public Member getMember(UUID memberId) {
        return membersList.stream()
                .filter(member -> member.getUserId().equals(memberId))
                .findFirst()
                .orElse(null);
    }

    public GroupResponse toGroupResponse() {
        return new GroupResponse(
                groupId,
                groupName,
                description,
                creatorId,
                timeCreated,
                membersList != null ? membersList.stream().map(Member::toMemberResponse).toList() : new ArrayList<>() // Convert to member response
        );
    }

    public boolean isMemberInGroup(UUID memberId) {
        return membersList.stream().anyMatch(member -> member.getUserId().equals(memberId));
    }
}
