package com.vatek.hrmtool.mapping.excel;

import com.vatek.hrmtool.dto.user.UserExcelDto;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.awt.font.TextMeasurer;

@Mapper(componentModel = "spring",uses = {TimesheetExcelMapping.class})
public interface UserExcelMapping extends EntityMapper<UserExcelDto, UserEntity> {
    @Override
    @Mappings({
            @Mapping(target = "name",source = "name"),
            @Mapping(target = "timeSheets",source = "timesheetEntities")
    })
    UserExcelDto toDto(UserEntity entity);
}
