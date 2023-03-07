package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "holiday")
@Getter
@Setter
public class HolidayEntity extends CommonEntity {
    @Column(columnDefinition = "DATE")
    private Instant holiday;
    @Column
    private String name;
    @Column
    private int duration;
}
