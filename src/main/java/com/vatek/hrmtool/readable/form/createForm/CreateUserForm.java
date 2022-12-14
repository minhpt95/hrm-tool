package com.vatek.hrmtool.readable.form.createForm;

import com.vatek.hrmtool.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserForm {

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String identityCard;

    @NotEmpty
    private String phoneNumber1;

    @NotEmpty
    private String currentAddress;

    @NotEmpty
    private String permanentAddress;

    private MultipartFile avatarImage;

    @NotEmpty
    private Collection<Role> roles;
}
