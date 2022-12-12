package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.readable.form.createForm.CreateUserForm;
import com.vatek.hrmtool.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
@AllArgsConstructor
@Log4j2
public class ProjectController {

    private ProjectService projectService;
    @PostMapping("/create")
    public ResponseDto<ProjectDto> createProject(CreateUserForm createUserForm){

//        ProjectDto projectDto = projectService;

        var projectDto = new ResponseDto<ProjectDto>();
        projectDto.setContent(new ProjectDto());
        projectDto.setMessage(ErrorConstant.Message.SUCCESS);
        projectDto.setErrorCode(ErrorConstant.Code.SUCCESS);
        projectDto.setErrorType(ErrorConstant.Type.SUCCESS);
        return projectDto;
    }

}
