package com.vatek.hrmtool.mapping;

import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.entity.PrivilegeEntity;
import com.vatek.hrmtool.entity.RoleEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.enumeration.Privilege;
import com.vatek.hrmtool.enumeration.Role;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapping extends EntityMapper<UserDto,UserEntity> {

    UserEntity toEntity(UserDto userDto);

    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "roles",expression = "java(getRoleFromEntity(userEntity))"),
            @Mapping(target = "privileges", expression = "java(getPrivilegeFromEntity(userEntity))")
    })
    UserDto toDto(UserEntity userEntity);

    default Collection<UserDto> toListDto(Collection<UserEntity> userEntities){
        return userEntities.stream().map(this::toDto).collect(Collectors.toList());
    }

    default Collection<Role> getRoleFromEntity(UserEntity entity) {
        return entity
                .getRoles()
                .stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toList());
    }

    default Collection<Privilege> getPrivilegeFromEntity(UserEntity entity){
        return entity
                .getRoles()
                .stream()
                .map(RoleEntity::getPrivileges)
                .flatMap(Collection::stream)
                .map(PrivilegeEntity::getPrivilege)
                .collect(Collectors.toList());
    }

}
