package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.TimesheetEntity;
import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.projection.TimesheetWorkingHourProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.List;

public interface TimesheetRepository extends JpaRepository<TimesheetEntity,Long>, JpaSpecificationExecutor<TimesheetEntity>  {
    List<TimesheetWorkingHourProjection> findByUserEntityIdAndWorkingDayAndStatusNot(Long userId, Instant workingDay, ApprovalStatus status);
}
