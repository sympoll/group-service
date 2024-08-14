package com.MTAPizza.Sympoll.groupmanagementservice.model.member;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.MemberResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.id.MemberId;
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
@IdClass(MemberId.class)
public class Member {
    @Id
    private String groupId;

    @Id
    private UUID userId;

    public MemberResponse toMemberResponse() {
        return new MemberResponse(
                userId
        );
    }
}
