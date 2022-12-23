package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.DateTimeEntity;
import com.vatek.hrmtool.enumeration.TypeDayOff;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "DayOff")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DayOffEntity extends DateTimeEntity {
    @EmbeddedId
    private DateOffId dateOffId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private RequestEntity request;

    @Embeddable
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DateOffId implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        @Column(columnDefinition = "date")
        private Instant dateOff;

        private Long userId;

        @Column
        @Enumerated(EnumType.STRING)
        private TypeDayOff typeDayoff;
    }
}


