package com.vatek.hrmtool.mapping;

import com.vatek.hrmtool.dto.timesheet.TimesheetDto;
import com.vatek.hrmtool.entity.TimesheetEntity;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {UserMapping.class,ProjectMapping.class})
public interface TimesheetMapping extends EntityMapper<TimesheetDto, TimesheetEntity> {
    @Override
    TimesheetDto toDto(TimesheetEntity entity);
}
