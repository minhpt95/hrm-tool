package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.mapping.ProjectMapping;
import com.vatek.hrmtool.readable.form.createForm.CreateProjectForm;
import com.vatek.hrmtool.respository.ProjectRepository;
import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.ProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
@Log4j2
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;

    private UserRepository userRepository;

    private ProjectMapping projectMapping;
    @Override
    public ProjectDto createProject(CreateProjectForm createProjectForm){

        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(createProjectForm.getProjectName());
        projectEntity.setDescription(createProjectForm.getProjectDescription());

        var projectManagerUser = userRepository.findUserEntityById(createProjectForm.getProjectManager());

        if(projectManagerUser == null){
            throw new ProductException(
                    ErrorResponse.builder()
                    .message(String.format(ErrorConstant.Message.NOT_FOUND,"Project Manager with id : " + createProjectForm.getProjectManager()))
                    .errorCode(ErrorConstant.Code.NOT_FOUND)
                    .errorType(ErrorConstant.Type.NOT_FOUND)
                    .build()
            );
        }

        projectEntity.setManagerUser(projectManagerUser);

        if(createProjectForm.getMemberId().size() > 0){
            var listMemberUser = userRepository.findUserEntitiesByIdIn(createProjectForm.getMemberId());
            projectEntity.setMemberUser(listMemberUser);
        }

        projectEntity.setCreatedBy(currentUser.getId());
        projectEntity.setCreatedTime(Instant.now());

        projectEntity.setProjectStatus(createProjectForm.getProjectStatus());

        projectRepository.save(projectEntity);

        return projectMapping.toDto(projectEntity);
    }
}
