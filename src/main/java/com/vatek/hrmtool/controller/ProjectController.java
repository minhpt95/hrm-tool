package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.readable.form.createForm.CreateProjectForm;
import com.vatek.hrmtool.readable.form.createForm.CreateUserForm;
import com.vatek.hrmtool.readable.form.updateForm.UpdateMemberProjectForm;
import com.vatek.hrmtool.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/project")
@AllArgsConstructor
@Log4j2
public class ProjectController {

    private ProjectService projectService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseDto<ProjectDto> createProject(CreateProjectForm createProjectForm){
        var projectDto = projectService.createProject(createProjectForm);

        return getProjectDtoResponseDto(projectDto);
    }


    @PreAuthorize("hasAnyRole('ROLE_PM')")
    @PostMapping("/ModifyMemberProject")
    public ResponseDto<ProjectDto> addMemberToProject(@RequestBody UpdateMemberProjectForm updateMemberProjectForm){
        var projectDto = projectService.updateMemberProject(updateMemberProjectForm);

        return getProjectDtoResponseDto(projectDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_PM')")
    @PostMapping("/getAllProjectPageable")
    public ListResponseDto<ProjectDto> getAllProjectPageable(Pageable pageable){
        return projectService.getProjectPageable(pageable);
    }

    private ResponseDto<ProjectDto> getProjectDtoResponseDto(ProjectDto projectDto) {
        var projectResponseDto = new ResponseDto<ProjectDto>();
        projectResponseDto.setContent(projectDto);
        projectResponseDto.setMessage(ErrorConstant.Message.SUCCESS);
        projectResponseDto.setErrorCode(ErrorConstant.Code.SUCCESS);
        projectResponseDto.setErrorType(ErrorConstant.Type.SUCCESS);
        return projectResponseDto;
    }

}
