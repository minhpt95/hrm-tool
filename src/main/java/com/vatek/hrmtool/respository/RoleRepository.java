package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.RoleEntity;
import com.vatek.hrmtool.entity.enumeration.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface RoleRepository extends JpaRepository<RoleEntity, Long>, JpaSpecificationExecutor<RoleEntity> {
    RoleEntity findByRole(Role role);

    Collection<RoleEntity> findByRoleIn(Collection<Role> roles);
}
