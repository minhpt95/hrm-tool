package com.vatek.hrmtool.mapping;

import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.entity.DayOffEntity;
import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.entity.RoleEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.enumeration.Role;

import com.vatek.hrmtool.mapping.common.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {UserMapping.class})
public interface RequestMapping extends EntityMapper<RequestDto, RequestEntity> {
    @Override
    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "dayOffGroupList",expression = "java(mapListDayOffToGroups(entity))")
    })
    RequestDto toDto(RequestEntity entity);

    default List<RequestDto.DayOffGroup> mapListDayOffToGroups(RequestEntity entity) {
        Function<RequestEntity,List<Instant>> selectorInstant = requestEntity -> requestEntity.getDayOffEntityList().stream().map(x -> x.getDayoffEntityId().getDateOff()).toList();
        return groupByAdjacent(entity.getDayOffEntityList().stream().map(DayOffEntity::getDayoffEntityId).toList());
    }

    default List<RequestDto.DayOffGroup> groupByAdjacent(List<DayOffEntity.DayOffEntityId> dayOffEntityIds)
    {
        List<RequestDto.DayOffGroup> dayOffGroupList = new ArrayList<>();

        if(dayOffEntityIds.size() == 0){
            return dayOffGroupList;
        }

        int start = 0;
        for (int i = 1; i < dayOffEntityIds.size(); i++) {
            if (dayOffEntityIds.get(i - 1).getDateOff().plus(1, ChronoUnit.DAYS).compareTo(dayOffEntityIds.get(i).getDateOff()) != 0 ) {
                dayOffGroupList.add(new RequestDto.DayOffGroup(dayOffEntityIds.get(start).getDateOff(),dayOffEntityIds.get(i - 1).getDateOff()));
                start = i;
            }
        }
        dayOffGroupList.add(new RequestDto.DayOffGroup(dayOffEntityIds.get(start).getDateOff(),dayOffEntityIds.get(dayOffEntityIds.size() - 1).getDateOff()));
        return dayOffGroupList;
    }


}
