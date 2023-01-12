package com.vatek.hrmtool.mapping.excel;

import com.vatek.hrmtool.dto.project.ProjectDto;
import com.vatek.hrmtool.dto.project.ProjectExcelDto;
import com.vatek.hrmtool.entity.ProjectEntity;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring",uses = {UserExcelMapping.class})
public interface ProjectExcelMapping extends EntityMapper<ProjectExcelDto, ProjectEntity> {
    @Override
    @Mappings({
            @Mapping(target = "name",source = "name"),
            @Mapping(target = "members",source = "members")

    })
    ProjectExcelDto toDto(ProjectEntity entity);
}
