package com.vatek.hrmtool.readable.response.employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeePageableRes {
    private String name;
    private String email;
    private String imageUrl;
    private String position;
    private String linkedinUrl;
    private String facebookUrl;
}
