package com.vatek.hrmtool.readable.response.news;

import com.vatek.hrmtool.dto.employee.EmployeeAuthorDto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CreateNewsRes {
    private Long id;
    private String title;
    private String content;
    private boolean active;
    private EmployeeAuthorDto createdBy;
    private Instant createdDate;
}
