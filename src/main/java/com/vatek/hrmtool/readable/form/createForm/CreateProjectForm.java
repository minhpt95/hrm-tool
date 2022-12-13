package com.vatek.hrmtool.readable.form.createForm;

import com.vatek.hrmtool.enumeration.ProjectStatus;
import com.vatek.hrmtool.validator.anotation.DateFormatConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectForm {
    @NotEmpty
    private String projectName;

    private String projectDescription;

    @NotNull
    private Long projectManager;
    private List<Long> memberId;
    @NotNull
    private ProjectStatus projectStatus;
    @DateFormatConstraint()
    private String startDate;
}
