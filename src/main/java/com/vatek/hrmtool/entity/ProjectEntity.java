package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.dto.ModifyListDto;
import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.ProjectStatus;
import com.vatek.hrmtool.readable.form.updateForm.UpdateMemberProjectForm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private Collection<UserEntity> memberUser;

    @Column
    private Instant endTime;

    public void addMemberToProject(UserEntity userEntity) {
        if(memberUser == null){
            memberUser = new ArrayList<>();
        }
        memberUser.add(userEntity);
        userEntity.getWorkingProject().add(this);
    }

    public void removeMemberFromProject(UserEntity userEntity) {
        memberUser.remove(userEntity);
        userEntity.getWorkingProject().remove(this);
    }
}
