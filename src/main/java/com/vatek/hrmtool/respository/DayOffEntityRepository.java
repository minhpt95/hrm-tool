package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.DayOffEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DayOffEntityRepository extends JpaRepository<DayOffEntity, DayOffEntity.DayOffEntityId>, JpaSpecificationExecutor<DayOffEntity> {
}