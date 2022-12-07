package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.entity.enumeration.RequestStatus;
import com.vatek.hrmtool.entity.enumeration.TypeDayOff;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;

@Entity(name = "Requests")
@Getter
@Setter
public class RequestEntity extends CommonEntity {
    @Column
    private String requestTitle;
    @Column
    private String requestReason;

    @Column
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column
    private Instant fromDate;

    @Column
    private Instant toDate;

    @Column
    private Boolean isMultipleDay;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeDayOff typeDayOff;

    @ManyToOne
    private UserEntity requester;
}