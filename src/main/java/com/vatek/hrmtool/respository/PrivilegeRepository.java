package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.PrivilegeEntity;
import com.vatek.hrmtool.enumeration.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;

public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long>, JpaSpecificationExecutor<PrivilegeEntity> {
    PrivilegeEntity findByPrivilege(Privilege privilege);

    Collection<PrivilegeEntity> findByPrivilegeIn(Collection<Privilege> privilegeList);
}
