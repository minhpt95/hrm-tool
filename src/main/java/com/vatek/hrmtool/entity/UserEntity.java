package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.TimesheetType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity extends CommonEntity {
    @Column
    private String name;
    @Column(unique = true,nullable = false)
    private String email;
    @Column
    private String password;
    @Column(unique = true,nullable = false)
    private String identityCard;
    @Column
    private String phoneNumber1;
    @Column
    private String avatarUrl;
    @Column
    private String currentAddress;
    @Column
    private String permanentAddress;
    @Column
    private String accessToken;
    @Column
    private boolean tokenStatus;
    @Column
    private boolean isEnabled;
    @Column String level;
    @Column String programLanguage;
    @Column
    private String position;
    @OneToMany(fetch = FetchType.LAZY,cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    },mappedBy = "managerUser")
    private Collection<ProjectEntity> projectManagement = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY,cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    },mappedBy = "userEntity")
    @OrderBy("workingDay asc")
    private Collection<TimesheetEntity> timesheetEntities = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY,cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH
    },mappedBy = "requester")
    private Collection<RequestEntity> userRequest = new ArrayList<>();
    @ManyToMany(fetch = FetchType.LAZY,cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH,
    })
    @JoinTable(
            name = "users_projects",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "project_id", referencedColumnName = "id"))
    private Collection<ProjectEntity> workingProject = new ArrayList<>();
    
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;

    @Transient
    private Collection<TimesheetEntity> normalHours = new ArrayList<>();

    @Transient
    private Collection<TimesheetEntity> overtimeHours = new ArrayList<>();

    @Transient
    private Collection<TimesheetEntity> bonusHours = new ArrayList<>();

    @PostLoad
    private void loadToTransientData(){
        this.normalHours = timesheetEntities.stream().filter(x -> x.getTimesheetType() == TimesheetType.NORMAL_WORKING).toList();
        this.overtimeHours = timesheetEntities.stream().filter(x -> x.getTimesheetType() == TimesheetType.OVERTIME).toList();
        this.bonusHours = timesheetEntities.stream().filter(x -> x.getTimesheetType() == TimesheetType.PROJECT_BONUS).collect(Collectors.toList());
    }

 }
