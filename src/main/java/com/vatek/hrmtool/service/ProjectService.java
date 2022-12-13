package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.enumeration.ProjectStatus;
import com.vatek.hrmtool.readable.form.createForm.CreateProjectForm;
import com.vatek.hrmtool.readable.form.updateForm.UpdateMemberProjectForm;
import org.springframework.data.domain.Pageable;


public interface ProjectService {
    ProjectDto createProject(CreateProjectForm createProjectForm);

    ProjectDto updateMemberProject(UpdateMemberProjectForm updateMemberProjectForm);

    ListResponseDto<ProjectDto> getProjectPageable(Pageable pageable);
}
