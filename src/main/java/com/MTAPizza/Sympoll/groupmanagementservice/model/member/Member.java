package com.MTAPizza.Sympoll.groupmanagementservice.model.member;

import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service.MemberDetailsResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.group.service.MemberResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.member.id.MemberId;
import com.MTAPizza.Sympoll.groupmanagementservice.dto.response.user.service.UserDataResponse;
import com.MTAPizza.Sympoll.groupmanagementservice.model.user.role.UserRole;
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
    @Column(name = "group_id")
    private String groupId;

    @Id
    @Column(name = "user_id")
    private UUID userId;

    public MemberResponse toMemberResponse() {
        return new MemberResponse(
                userId);
    }

    public MemberDetailsResponse toMemberDetailsResponse(UserDataResponse userDataResponse, String userRole) {
        return new MemberDetailsResponse(
                userDataResponse,
                userRole
        );
    }
}
