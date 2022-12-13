package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.ModifyListDto;
import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.Action;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.mapping.ProjectMapping;
import com.vatek.hrmtool.readable.form.createForm.CreateProjectForm;
import com.vatek.hrmtool.readable.form.updateForm.UpdateMemberProjectForm;
import com.vatek.hrmtool.respository.ProjectRepository;
import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.ProjectService;
import com.vatek.hrmtool.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;

    private UserRepository userRepository;

    private ProjectMapping projectMapping;

    @Override
    @Transactional
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
            for(var memberUser : listMemberUser){
                projectEntity.addMemberToProject(memberUser);
            }
        }

        projectEntity.setCreatedBy(currentUser.getId());
        projectEntity.setCreatedTime(Instant.now());

        projectEntity.setProjectStatus(createProjectForm.getProjectStatus());

        projectEntity = projectRepository.saveAndFlush(projectEntity);

        return projectMapping.toDto(projectEntity);
    }

    @Override
    @Transactional
    public ProjectDto updateMemberProject(UpdateMemberProjectForm updateMemberProjectForm){
        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var projectEntity = projectRepository.findById(updateMemberProjectForm.getId()).orElseThrow(
                () ->
                new ProductException(ErrorResponse.builder()
                .message(String.format(ErrorConstant.Message.NOT_FOUND,"Project id : " + updateMemberProjectForm.getId()))
                .errorCode(ErrorConstant.Code.NOT_FOUND)
                .errorType(ErrorConstant.Type.NOT_FOUND)
                .build())
        );

        List<Long> removeMember = updateMemberProjectForm
                .getMember()
                .stream()
                .filter(x -> x.getAction() == Action.DELETE)
                .map(ModifyListDto::getId)
                .toList();

        List<Long> addMemberId = updateMemberProjectForm
                .getMember()
                .stream()
                .filter(x -> x.getAction() == Action.ADD)
                .map(ModifyListDto::getId)
                .filter(id -> !projectEntity.getMemberUser().stream().map(CommonEntity::getId).toList().contains(id))
                .toList();

        var removeMemberEntity = userRepository.findUserEntitiesByIdIn(removeMember);

        var addMemberEntity = userRepository.findUserEntitiesByIdIn(addMemberId);

        for(var rm : removeMemberEntity){
            projectEntity.removeMemberFromProject(rm);
        }

        for(var am : addMemberEntity){
            projectEntity.addMemberToProject(am);
        }

        projectEntity.setModifiedBy(currentUser.getId());

        projectEntity.setModifiedTime(Instant.now());

        return projectMapping.toDto(projectRepository.save(projectEntity));
    }

    @Override
    @Transactional
    public ListResponseDto<ProjectDto> getProjectPageable(Pageable pageable) {
        Page<ProjectEntity> userEntityPage = projectRepository.findAll(CommonUtil.buildPageable(pageable.getPageNumber(), pageable.getPageSize()));
        Page<ProjectDto> userDtoPage = userEntityPage.map(projectMapping::toDto);
        ListResponseDto<ProjectDto> result = new ListResponseDto<>();
        return result.buildResponseList(userDtoPage, pageable.getPageSize(), pageable.getPageNumber());
    }
}
