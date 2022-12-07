package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.entity.enumeration.ProjectStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;

@Entity
@Table(name = "projects")
@Getter
@Setter
public class ProjectEntity extends CommonEntity {

    @Column
    private String name;

    @Column
    private Boolean isDelete;

    @Column
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_user")
    private UserEntity managerUser;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "workingProject")
    private Collection<UserEntity> memberUser;

    @Column
    private Instant endTime;
}
