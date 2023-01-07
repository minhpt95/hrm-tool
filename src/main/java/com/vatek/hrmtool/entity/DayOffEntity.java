package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.enumeration.TypeDayOff;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "day_off")
@Getter
@Setter
public class DayOffEntity {

    @EmbeddedId
    private DayOffEntityId dayoffEntityId;
    @Column
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private RequestEntity requestEntity;

    @Embeddable
    @Getter
    @Setter
    public static class DayOffEntityId implements Serializable {
        @Serial
        private static final long serialVersionUID = -6491357190187436940L;

        @Column(columnDefinition = "DATE")
        private Instant dateOff;

        @Column
        @Enumerated(EnumType.STRING)
        private TypeDayOff typeDayOff;

        @Column
        private Long userId;
    }
}


