package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.jwt.JwtResponse;
import com.vatek.hrmtool.readable.form.LoginForm;
import com.vatek.hrmtool.readable.form.create.CreateUserForm;
import com.vatek.hrmtool.readable.form.create.RegisterUserForm;
import com.vatek.hrmtool.service.UserService;
import com.vatek.hrmtool.util.DateUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*",maxAge = 3600)
@RestController
@AllArgsConstructor
@Log4j2
@ApiOperation(value = "Login Controller", notes = "Auth controller without bearer token")
@RequestMapping("/api/auth")
public class AuthController {

    final UserService userService;

    @ApiOperation(value = "Login API", notes = "Returns token and refresh token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login Successfully"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
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
