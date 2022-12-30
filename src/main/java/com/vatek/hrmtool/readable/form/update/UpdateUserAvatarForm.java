package com.vatek.hrmtool.readable.form.update;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class UpdateUserAvatarForm {
    @NotNull
    private Long id;

    @NotNull
    private MultipartFile avatarImage;
}
