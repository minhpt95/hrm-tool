package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

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
    private String phoneNumber2;
    @Column
    private String currentAddress;
    @Column
    private String permanentAddress;
    @Column
    private String description;
    @Column
    private String accessToken;
    @Column
    private boolean tokenStatus;
    @Column
    private boolean isEnabled;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;
}