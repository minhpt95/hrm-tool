package com.vatek.hrmtool.entity;

import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.entity.enumeration.Privilege;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "privileges")
@Getter
@Setter
public class PrivilegeEntity extends CommonEntity {
    @Enumerated(EnumType.STRING)
    private Privilege privilege;

    @ManyToMany(mappedBy = "privileges")
    private Collection<RoleEntity> roles;
}
