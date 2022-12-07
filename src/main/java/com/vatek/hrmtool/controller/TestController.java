package com.vatek.hrmtool.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @PreAuthorize("hasAnyRole('ROLE_STAFF')")
    @RequestMapping(value = "/contactUs", method = RequestMethod.GET)
    @ResponseBody
    public String contactUs() {
        return "redirect:/contactUs/";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @RequestMapping(value = "/system", method = RequestMethod.GET)
    @ResponseBody
    public String system() {
        return "redirect:/system/";
    }
}
