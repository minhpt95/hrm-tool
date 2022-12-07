package com.vatek.hrmtool.readable.form.updateForm;

import com.vatek.hrmtool.readable.form.createForm.CreateUserForm;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateUserForm extends CreateUserForm {
    @NotNull
    private Long id;
}
