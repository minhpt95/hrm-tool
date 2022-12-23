package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.RequestStatus;
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

    @Column
    @Enumerated(EnumType.STRING)
    private TypeRequest typeRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_request_id")
    private UserEntity requester;

    @OneToMany(fetch = FetchType.LAZY,cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    },mappedBy = "request")
    private List<DayOffEntity> dayOffEntities = new ArrayList<>();
}
