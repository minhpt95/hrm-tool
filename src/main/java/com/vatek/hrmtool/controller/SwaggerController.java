package com.vatek.hrmtool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class SwaggerController {
    @GetMapping()
    public String swaggerUi() {
        return "redirect:/swagger-ui/index.html";
    }
}
