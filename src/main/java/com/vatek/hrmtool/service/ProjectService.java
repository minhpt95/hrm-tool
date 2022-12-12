package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.readable.form.createForm.CreateProjectForm;

public interface ProjectService {
    ProjectDto createProject(CreateProjectForm createProjectForm);
}
