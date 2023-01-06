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
@AllArgsConstructor
@NoArgsConstructor
public class DayOffEntity {

    @Column
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;
    @EmbeddedId
    private DayoffEntityId dayoffEntityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private RequestEntity requestEntity;

    @Embeddable
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class DayoffEntityId implements Serializable {
        @Serial
        private static final long serialVersionUID = -6491357190187436940L;

        @Column(columnDefinition = "DATE")
        private Instant dateOff;

        @Column
        @Enumerated(EnumType.STRING)
        private TypeDayOff typeDayOff;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DayoffEntityId dayoffEntityId1 = (DayoffEntityId) o;
            if (dateOff.compareTo(dayoffEntityId1.dateOff) != 0) return false;

            switch (this.typeDayOff){
                case FULL -> {
                    return true;
                }
                case MORNING, AFTERNOON -> {
                    if(dayoffEntityId1.getTypeDayOff() == TypeDayOff.FULL){
                        return true;
                    }
                    return this.typeDayOff == dayoffEntityId1.typeDayOff;
                }
                default -> {
                    return false;
                }
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.dateOff, this.typeDayOff);
        }
    }
}
