package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity extends CommonEntity {

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private Collection<UserEntity> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Collection<PrivilegeEntity> privileges;
}
