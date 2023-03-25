package com.vatek.hrmtool.mapping.dto;

import com.vatek.hrmtool.mapping.UserMapping;
import com.vatek.hrmtool.mapping.common.EntityMapper;
import com.vatek.hrmtool.readable.form.create.CreateUserForm;
import com.vatek.hrmtool.readable.form.create.RegisterUserForm;
import org.mapstruct.Mapper;
import org.w3c.dom.Entity;

@Mapper(componentModel = "spring",uses = {UserMapping.class})
public interface CreateUserMapping extends EntityMapper<RegisterUserForm, CreateUserForm> {
    CreateUserForm toCreateFrom(RegisterUserForm form);
}
