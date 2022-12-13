package com.vatek.hrmtool.mapping;

import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",uses = {UserMapping.class})
public interface ProjectMapping extends EntityMapper<ProjectDto, ProjectEntity> {

    ProjectEntity toEntity(ProjectDto projectDto);

    @Mappings({
            @Mapping(target = "id",source = "id"),
    })
    ProjectDto toDto(ProjectEntity projectEntity);
}
