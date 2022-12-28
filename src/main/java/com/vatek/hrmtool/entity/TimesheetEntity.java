package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.AdditionalHourType;
import com.vatek.hrmtool.enumeration.RequestStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "timesheet")
@Getter
@Setter
public class TimesheetEntity extends CommonEntity {
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private Integer workingHours;
    @Column
    private Integer additionalHours;
    @Column
    private AdditionalHourType additionalHourType;
    @Column(columnDefinition = "DATE")
    private Instant workingDay;
    @Column
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private ProjectEntity projectEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
