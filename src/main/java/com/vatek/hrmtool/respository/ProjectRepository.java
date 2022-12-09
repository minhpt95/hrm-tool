package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>, JpaSpecificationExecutor<ProjectEntity> {
}
