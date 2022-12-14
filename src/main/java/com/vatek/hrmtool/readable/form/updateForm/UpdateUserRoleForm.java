package com.vatek.hrmtool.readable.form.updateForm;

import com.vatek.hrmtool.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleForm {

    @NotEmpty
    private Long id;

    @NotEmpty
    private Role role;
}
