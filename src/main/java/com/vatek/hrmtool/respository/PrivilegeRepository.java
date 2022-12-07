package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.PrivilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long>, JpaSpecificationExecutor<PrivilegeEntity> {
    PrivilegeEntity findByName(String name);
}
