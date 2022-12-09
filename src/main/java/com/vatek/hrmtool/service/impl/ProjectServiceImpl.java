package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.dto.ProjectDto;
import com.vatek.hrmtool.readable.form.createForm.CreateProjectForm;
import com.vatek.hrmtool.respository.ProjectRepository;
import com.vatek.hrmtool.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class ProjectServiceImpl {
    private ProjectRepository projectRepository;

    public ProjectDto createProjectDto(CreateProjectForm createProjectForm){
        return null;
    }
}
