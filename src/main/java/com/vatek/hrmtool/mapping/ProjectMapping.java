package com.vatek.hrmtool.mapping;

import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.entity.PrivilegeEntity;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.entity.RoleEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.entity.enumeration.Privilege;
import com.vatek.hrmtool.entity.enumeration.Role;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {UserMapping.class})
public interface ProjectMapping extends EntityMapper<ProjectDto, ProjectEntity> {

    ProjectEntity toEntity(ProjectDto projectDto);

    ProjectDto toDto(ProjectEntity projectEntity);
}
