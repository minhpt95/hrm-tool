package com.vatek.hrmtool.mapping;

import com.vatek.hrmtool.dto.request.RequestDto;
import com.vatek.hrmtool.entity.DayOffEntity;
import com.vatek.hrmtool.entity.RequestEntity;
import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring",uses = {UserMapping.class})
public interface RequestMapping extends EntityMapper<RequestDto, RequestEntity> {
    @Override
    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "dayOffGroupList",expression = "java(groupByAdjacent(entity))")
    })
    RequestDto toDto(RequestEntity entity);

    default List<RequestDto.DayOffGroup> groupByAdjacent(RequestEntity entity){
        Function<RequestEntity, List<DayOffEntity>> selector = requestEntity ->
                requestEntity
                        .getDayOffEntityList()
                        .stream()
                        .toList();

        BiPredicate<DayOffEntity,DayOffEntity> comparer = (x, y) -> {
            Instant instantX = x.getDayoffEntityId().getDateOff();
            Instant instantY = y.getDayoffEntityId().getDateOff();

            ApprovalStatus approvalStatusX = x.getStatus();
            ApprovalStatus approvalStatusY = y.getStatus();

            return instantX.minus(1,ChronoUnit.DAYS).compareTo(instantY) == 0 && approvalStatusX == approvalStatusY;
        };

        return groupByAdjacent(entity,selector,comparer);
    }

    default List<RequestDto.DayOffGroup> groupByAdjacent
            (
                RequestEntity requestEntity,
                Function<RequestEntity, List<DayOffEntity>> fieldSelector,
                BiPredicate<DayOffEntity,DayOffEntity> comparer
            ){
        var inputList = fieldSelector.apply(requestEntity);
        var count = inputList.size();
        var graph = new ArrayList<Integer>();

        for (DayOffEntity currentValue : inputList) {
            var previous = IntStream
                    .range(0, count)
                    .filter(t -> comparer.test((currentValue), inputList.get(t)))
                    .findFirst()
                    .orElse(-1);

            graph.add(previous);
        }

        var map = getIntegerHashMap(count, graph);

        return map.entrySet().stream().map(x -> new RequestDto.DayOffGroup(
                inputList.get(x.getKey()).getDayoffEntityId().getDateOff(),
                inputList.get(x.getValue()).getDayoffEntityId().getDateOff(),
                inputList.get(x.getKey()).getDayoffEntityId().getTypeDayOff(),
                inputList.get(x.getKey()).getStatus()
        )).toList();
    }

    private static HashMap<Integer, Integer> getIntegerHashMap(int count, ArrayList<Integer> graph) {
        var temp = new HashSet<Integer>();

        var map = new HashMap<Integer,Integer>();

        for(int i = 0; i < count; i++){
            var current = i;
            if(temp.contains(current))
                continue;
            var rootFound = false;
            while (!rootFound){
                var prev = graph.get(current);
                temp.add(current);
                if(prev != -1)
                {
                    current = prev;
                }
                else
                {
                    rootFound = true;
                }
            }
            map.put(current,i);
        }
        return map;
    }

}
