package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.jwt.JwtResponse;
import com.vatek.hrmtool.readable.form.LoginForm;
import com.vatek.hrmtool.readable.form.createForm.CreateUserForm;
import com.vatek.hrmtool.service.UserService;
import com.vatek.hrmtool.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/auth")
public class AuthController {

    final UserService userService;

    @PostMapping(value = "/register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto<UserDto> registerUser(@Valid @ModelAttribute CreateUserForm createUserForm) {
        var createUserDto = userService.createUser(createUserForm);

        var responseDto = new ResponseDto<UserDto>();
        responseDto.setContent(createUserDto);
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);

        return responseDto;
    }

    @PostMapping(value = "/login")
    public ResponseDto<?> authenticateUser(@RequestBody LoginForm loginForm){
        JwtResponse jwtResponse = userService.authenticateUser(loginForm);

        var responseDto = new ResponseDto<JwtResponse>();
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setContent(jwtResponse);

        return responseDto;
    }

    @PutMapping(value = "/activate-email/{id}")
    public  ResponseDto<?> activateEmail(@PathVariable Long id){
        ResponseDto<?> responseDto = new ResponseDto<>();

        userService.activateEmail(id, DateUtil.getInstantNow());

        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);

        return responseDto;
    }

    @PostMapping(value = "/forgot/{email}")
    public ResponseDto<?> forgotPassword(@PathVariable(name = "email") String email){
        userService.forgotPassword(email);

        var responseDto = new ResponseDto<>();
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);

        return responseDto;
    }
}
