package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.CronEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CronRepository extends JpaRepository<CronEntity,Long>, JpaSpecificationExecutor<CronEntity> {
    Optional<CronEntity> getCronEntityByCronCode(String cronCode);

    Optional<CronEntity> getCronEntityByCronName(String cronName);

    List<CronEntity> getAllByCronNameContainsIgnoreCase(String cronName);


}
