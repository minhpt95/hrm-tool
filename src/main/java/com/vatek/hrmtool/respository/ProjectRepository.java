package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>, JpaSpecificationExecutor<ProjectEntity> {
    Optional<ProjectEntity> findById(Long id);

}
