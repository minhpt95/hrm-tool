package com.vatek.hrmtool.readable.form.createForm;

import com.vatek.hrmtool.entity.enumeration.Privilege;
import com.vatek.hrmtool.entity.enumeration.Role;
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

    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String identityCard;

    @NotEmpty
    private String phoneNumber1;
//    private String phoneNumber2;

    @NotEmpty
    private String currentAddress;

    @NotEmpty
    private String permanentAddress;
//    private String description;

    private MultipartFile avatarImage;

    @NotEmpty
    private Collection<Role> roles;
}
