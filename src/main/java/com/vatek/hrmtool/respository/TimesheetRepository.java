package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.entity.TimesheetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TimesheetRepository extends JpaRepository<TimesheetEntity,Long>, JpaSpecificationExecutor<TimesheetEntity>  {
}
