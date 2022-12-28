package com.vatek.hrmtool.dto.project;

import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.enumeration.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private String description;
    private Boolean isDelete;
    private ProjectStatus projectStatus;
    private UserDto managerUser;
    private Collection<UserDto> members;
    @Column
    private Instant endTime;
}
