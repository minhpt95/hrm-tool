package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "cron")
@Getter
@Setter
public class CronEntity extends CommonEntity {
    @Column(name = "cron_code", unique = true)
    private String cronCode;

    @Column
    private String cronName;

    @Column
    private String cronExpression;
}
