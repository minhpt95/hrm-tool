package com.vatek.hrmtool.mapping;

import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapping extends EntityMapper<UserDto,UserEntity> {


    UserEntity toDto(UserDto userDto);


    UserDto toEntity(UserEntity userEntity);
}
