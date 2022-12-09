package com.vatek.hrmtool.readable.form.createForm;

import com.vatek.hrmtool.entity.enumeration.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectForm {
    private String projectTitle;
    private String projectDescription;

    private Long projectManager;
    private List<Long> memberId;

    private ProjectStatus projectStatus;

}
