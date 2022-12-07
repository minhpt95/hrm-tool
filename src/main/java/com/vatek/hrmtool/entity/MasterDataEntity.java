package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "master_data")
@Getter
@Setter
public class MasterDataEntity extends CommonEntity {

    @Column
    private String type;

    @Column
    private String code;

    @Column
    private String name;

    @Column
    private boolean active;

}
