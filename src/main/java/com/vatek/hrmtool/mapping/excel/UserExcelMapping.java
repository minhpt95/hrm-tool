package com.vatek.hrmtool.mapping.excel;

import com.vatek.hrmtool.dto.user.UserExcelDto;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",uses = {TimesheetExcelMapping.class})
public interface UserExcelMapping extends EntityMapper<UserExcelDto, UserEntity> {
    @Override
    @Mappings({
            @Mapping(target = "name",source = "name"),
            @Mapping(target = "normalHours",source = "normalHours"),
            @Mapping(target = "overtimeHours",source = "overtimeHours")
    })
    UserExcelDto toDto(UserEntity entity);
}
