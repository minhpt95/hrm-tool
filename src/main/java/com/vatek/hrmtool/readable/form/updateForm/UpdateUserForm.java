package com.vatek.hrmtool.readable.form.updateForm;

import com.vatek.hrmtool.readable.form.createForm.CreateUserForm;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateUserForm {
    @NotNull
    private Long id;

    private String name;

    private String email;

    private String password;

    private String identityCard;

    private String phoneNumber1;

    private String currentAddress;

    private String permanentAddress;
}
