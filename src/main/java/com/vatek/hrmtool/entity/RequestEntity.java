package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.RequestStatus;
import com.vatek.hrmtool.enumeration.TypeDayOff;
import com.vatek.hrmtool.enumeration.TypeRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

    @Column(columnDefinition = "DATE")
    private Instant fromDate;

    @Column(columnDefinition = "DATE")
    private Instant toDate;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeDayOff typeDayOff;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private UserEntity requester;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeRequest typeRequest;
}
