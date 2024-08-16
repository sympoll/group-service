package com.MTAPizza.Sympoll.groupmanagementservice.model.member.id;


import com.MTAPizza.Sympoll.groupmanagementservice.model.member.Member;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
public class MemberId implements Serializable {

    private String groupId;

    private UUID userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberId that = (MemberId) o;
        return Objects.equals(groupId, that.groupId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, userId);
    }
}
