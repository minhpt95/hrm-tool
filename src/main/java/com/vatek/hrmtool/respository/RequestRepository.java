package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RequestRepository extends JpaRepository<RequestEntity,Long>, JpaSpecificationExecutor<RequestEntity> {
}
