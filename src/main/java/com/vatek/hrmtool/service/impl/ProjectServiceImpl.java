package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.ModifyListDto;
import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.enumeration.Action;
import com.vatek.hrmtool.enumeration.Role;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.mapping.ProjectMapping;
import com.vatek.hrmtool.mapping.excel.ProjectExcelMapping;
import com.vatek.hrmtool.readable.form.create.CreateProjectForm;
import com.vatek.hrmtool.readable.form.update.UpdateMemberProjectForm;
import com.vatek.hrmtool.respository.ProjectRepository;
import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.ProjectService;
import com.vatek.hrmtool.util.CommonUtils;
import com.vatek.hrmtool.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class ProjectServiceImpl implements ProjectService {
    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ProjectMapping projectMapping;
    private ProjectExcelMapping projectExcelMapping;
    private ExcelTemplateServiceImpl excelTemplateService;
    @Override
    @Transactional
    public ProjectDto createProject(CreateProjectForm createProjectForm){

        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var projectEntity = new ProjectEntity();
        projectEntity.setName(createProjectForm.getProjectName());
        projectEntity.setDescription(createProjectForm.getProjectDescription());

        var projectManagerUser = userRepository.findUserEntityById(createProjectForm.getProjectManager());

        if(projectManagerUser == null){
            throw new ProductException(
                    ErrorResponse.builder()
                    .message(String.format(ErrorConstant.Message.NOT_FOUND,"Project Manager with id : " + createProjectForm.getProjectManager()))
                    .code(ErrorConstant.Code.NOT_FOUND)
                    .type(ErrorConstant.Type.NOT_FOUND)
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
        projectEntity.setCreatedTime(DateUtils.getInstantNow());

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
                new ProductException(
                        ErrorResponse.builder()
                                .message(String.format(ErrorConstant.Message.NOT_FOUND,"Project id : " + updateMemberProjectForm.getId()))
                                .code(ErrorConstant.Code.NOT_FOUND)
                                .type(ErrorConstant.Type.NOT_FOUND)
                                .build()
                )
        );

        if(
                currentUser.getAuthorities().stream().noneMatch(x -> Objects.equals(x.getAuthority(), Role.Code.ADMIN)) &&
                !Objects.equals(currentUser.getId(),projectEntity.getManagerUser().getId())
        ){
            throw new AccessDeniedException(ErrorConstant.Message.CANNOT_UPDATE_ANOTHER_PROJECT);
        }

        var removeMemberId = updateMemberProjectForm
                .getMember()
                .stream()
                .filter(x -> x.getAction() == Action.DELETE)
                .map(ModifyListDto::getId)
                .toList();

        var addMemberId = updateMemberProjectForm
                .getMember()
                .stream()
                .filter(x -> x.getAction() == Action.ADD)
                .map(ModifyListDto::getId)
                .filter(id -> !projectEntity.getMembers().stream().map(CommonEntity::getId).toList().contains(id))
                .toList();

        var removeMemberEntity = userRepository.findUserEntitiesByIdIn(removeMemberId);

        var addMemberEntity = userRepository.findUserEntitiesByIdIn(addMemberId);

        for(var rm : removeMemberEntity){
            projectEntity.removeMemberFromProject(rm);
        }

        for(var am : addMemberEntity){
            projectEntity.addMemberToProject(am);
        }

        projectEntity.setModifiedBy(currentUser.getId());

        projectEntity.setModifiedTime(DateUtils.getInstantNow());

        return projectMapping.toDto(projectRepository.save(projectEntity));
    }

    @Override
    @Transactional
    public ListResponseDto<ProjectDto> getProjectPageable(Pageable pageable) {

        var projectEntities = projectRepository.findAll(CommonUtils.buildPageable(pageable.getPageNumber(), pageable.getPageSize()));

        var projectDtos = projectEntities.map(projectMapping::toDto);

        return new ListResponseDto<>(projectDtos, pageable.getPageSize(), pageable.getPageNumber());
    }

    @Override
    @Transactional
    public ListResponseDto<ProjectDto> getProjectByManager(Pageable pageable) {
        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<ProjectEntity> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("managerUser").get("id"),currentUser.getId());

        return getProjectDtoListResponseDto(pageable, specification);
    }

    @Override
    @Transactional
    public ListResponseDto<ProjectDto> getWorkingProjectByUser(Pageable pageable) {
        var currentUser = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Specification<ProjectEntity> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("memberUser").get("id"),currentUser.getId());

        return getProjectDtoListResponseDto(pageable, specification);
    }

    @Override
    @Transactional
    public byte[] exportTimesheetByProject(Long projectId) {
        var projectEntity = projectRepository.findById(projectId).orElse(null);

        if (projectEntity == null) {
            throw new ProductException(
                    ErrorResponse
                            .builder()
                            .type(ErrorConstant.Type.NOT_FOUND)
                            .code(ErrorConstant.Code.NOT_FOUND)
                            .message(String.format(ErrorConstant.Message.NOT_FOUND, "Project id : " + projectId))
                            .build()
            );
        }

        var projectExcelDto = projectExcelMapping.toDto(projectEntity);


        Map<String, Object> contextMap = Map.of(
                "project", projectExcelDto
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        excelTemplateService.createDocument(outputStream, "project-timesheet", contextMap);

        return outputStream.toByteArray();
    }

    private ListResponseDto<ProjectDto> getProjectDtoListResponseDto(Pageable pageable, Specification<ProjectEntity> specification) {
        var projectEntities = projectRepository.findAll(
                specification,
                CommonUtils.buildPageable(pageable.getPageNumber(), pageable.getPageSize())
        );

        var projectDtos = projectEntities.map(projectMapping::toDto);

        return new ListResponseDto<>(projectDtos,pageable.getPageSize(), pageable.getPageNumber());
    }
}
