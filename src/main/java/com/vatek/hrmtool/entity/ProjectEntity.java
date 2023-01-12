package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.ProjectStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "projects")
@Getter
@Setter
public class ProjectEntity extends CommonEntity {
    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Boolean isDelete;

    @Column
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_user")
    private UserEntity managerUser;

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "workingProject",cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
    })
    private Collection<UserEntity> members;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "projectEntity")
    @OrderBy("workingDay asc")
    private Collection<TimesheetEntity> timesheetEntities = new ArrayList<>();

    @Column
    private Instant startTime;
    @Column
    private Instant endTime;

    public void addMemberToProject(UserEntity userEntity) {
        if(members == null){
            members = new ArrayList<>();
        }
        members.add(userEntity);
        userEntity.getWorkingProject().add(this);
    }

    public void removeMemberFromProject(UserEntity userEntity) {
        members.remove(userEntity);
        userEntity.getWorkingProject().remove(this);
    }
}
