package com.vatek.hrmtool.readable.form.update;

import com.vatek.hrmtool.readable.form.create.CreateProjectForm;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateProjectForm extends CreateProjectForm {
    @NotNull
    private Long id;
}
