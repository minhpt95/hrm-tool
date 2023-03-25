package com.vatek.hrmtool.mapping.excel;

import com.vatek.hrmtool.dto.timesheet.TimesheetExcelDto;
import com.vatek.hrmtool.entity.TimesheetEntity;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import com.vatek.hrmtool.util.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface TimesheetExcelMapping extends EntityMapper<TimesheetExcelDto, TimesheetEntity> {
    @Override
    @Mappings({
            @Mapping(target = "date",expression = "java(convertInstantToDateString(entity.getWorkingDay()))"),
            @Mapping(target = "taskDescription",source = "description")
    })
    TimesheetExcelDto toDto(TimesheetEntity entity);

    default String convertInstantToDateString(Instant instant){
        return DateUtils.convertInstantToStringDate(instant);
    }
}

