package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.jwt.JwtResponse;
import com.vatek.hrmtool.readable.form.LoginForm;
import com.vatek.hrmtool.readable.form.create.RegisterUserForm;
import com.vatek.hrmtool.service.UserService;
import com.vatek.hrmtool.util.DateUtils;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/auth")
public class AuthController {

    final UserService userService;

    @PostMapping(value = "/login")
    public ResponseDto<JwtResponse> authenticateUser(@RequestBody LoginForm loginForm){
        JwtResponse jwtResponse = userService.authenticateUser(loginForm);

        var responseDto = new ResponseDto<JwtResponse>();
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setContent(jwtResponse);

        return responseDto;
    }

    @PostMapping(value = "register-user",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<UserDto> registerUser(@ModelAttribute RegisterUserForm registerUserForm){
        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        responseDto.setContent(userService.registerUser(registerUserForm));
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);

        return responseDto;
    }

    @PutMapping(value = "/activate-email/{id}")
    public  ResponseDto<Boolean> activateEmail(@PathVariable Long id){
        userService.activateEmail(id, DateUtils.getInstantNow());

        var responseDto = new ResponseDto<Boolean>();
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);

        return responseDto;
    }

    @PostMapping(value = "/forgot/{email}")
    public ResponseDto<Boolean> forgotPassword(@PathVariable(name = "email") String email){
        userService.forgotPassword(email);

        var responseDto = new ResponseDto<Boolean>();
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);

        return responseDto;
    }
}
