package com.MTAPizza.Sympoll.groupmanagementservice.model;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberResponse;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "members")
@NoArgsConstructor
@Data
public class Member {
    @Column(name = "group_id")
    private String groupId;

    @Id
    @Column(name = "user_id")
    private UUID userId;

    public MemberResponse toMemberResponse() {
        return new MemberResponse(
                userId
        );
    }
}
