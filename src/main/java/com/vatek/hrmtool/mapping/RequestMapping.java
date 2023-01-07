package com.vatek.hrmtool.mapping;

import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring",uses = {UserMapping.class})
public interface RequestMapping extends EntityMapper<RequestDto, RequestEntity> {
    @Override
    @Mappings({
            @Mapping(target = "id",source = "id"),
//            @Mapping(target = "dayOffGroupList",expression = "")
    })
    RequestDto toDto(RequestEntity entity);


}
