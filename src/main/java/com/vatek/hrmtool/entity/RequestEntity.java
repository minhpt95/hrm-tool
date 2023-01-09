package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.enumeration.TypeDayOff;
import com.vatek.hrmtool.enumeration.TypeRequest;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import javax.persistence.*;
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

    @Where(clause = "status <> 'REJECTED'")
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "requestEntity",cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    })
    private List<DayOffEntity> dayOffEntityList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private UserEntity requester;

    @Column
    @Enumerated(EnumType.STRING)
    private TypeRequest typeRequest;
}
