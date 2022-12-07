package com.vatek.hrmtool.readable.request.news;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRequestHireDeveloperReq {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String company;
    private Long companySizeId;
    private String comment;
}
