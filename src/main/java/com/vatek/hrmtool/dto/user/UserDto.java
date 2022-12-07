package com.vatek.hrmtool.dto.user;
import com.vatek.hrmtool.entity.PrivilegeEntity;
import com.vatek.hrmtool.entity.enumeration.Privilege;
import com.vatek.hrmtool.entity.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String identityCard;
    private String phoneNumber1;
    private String phoneNumber2;
    private String currentAddress;
    private String permanentAddress;
    private String description;
    private boolean isEnabled;
    private Collection<Role> roles;
    private Collection<Privilege> privileges;
}
