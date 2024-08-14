package com.MTAPizza.Sympoll.groupmanagementservice.model;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "members")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Member {
    @Id
    @Column(name = "group_id")
    private String groupId;

    @Column(name = "user_id")
    private UUID userId;

    public MemberResponse toMemberResponse() {
        return new MemberResponse(
                userId
        );
    }
}
