package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.jwt.JwtProvider;
import com.vatek.hrmtool.jwt.payload.response.TokenRefreshResponse;
import com.vatek.hrmtool.readable.form.updateForm.UpdateUserForm;
import com.vatek.hrmtool.readable.request.ChangePasswordReq;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.RefreshTokenService;
import com.vatek.hrmtool.service.UserService;

import com.vatek.hrmtool.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
@Log4j2
public class UserController {

    final
    PasswordEncoder encoder;
    final
    UserService userService;

    final
    JavaMailSender emailSender;

    final
    JwtProvider jwtProvider;

    final
    RefreshTokenService refreshTokenService;

    @PutMapping(value = "/activateEmail/{id}")
    public  ResponseDto<Boolean> activateEmail(@PathVariable Long id){
        ResponseDto<Boolean> responseDto = new ResponseDto<>();

        responseDto.setContent(userService.activateEmail(id, DateUtil.getInstantNow()));
        responseDto.setErrorCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setErrorType(ErrorConstant.Type.SUCCESS);
        
        return responseDto;
    }

    @PutMapping(value = "/changePassword")
    public  ResponseDto<Boolean> changePassword(@Valid @RequestBody ChangePasswordReq changePasswordReq){
        ResponseDto<Boolean> responseDto = new ResponseDto<>();
        
        responseDto.setContent(userService.changePassword(changePasswordReq));
        responseDto.setErrorCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setErrorType(ErrorConstant.Type.SUCCESS);
        
        return responseDto;
    }

    @PutMapping(value = "/updateUser")
    public ResponseDto<UserDto> updateUser (@RequestBody UpdateUserForm form) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        responseDto.setContent(userService.updateUser(form));
        responseDto.setErrorCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setErrorType(ErrorConstant.Type.SUCCESS);

        return responseDto;
    }

    @PostMapping("/logout")
    public ResponseDto<?> logout(HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new ProductException(
                    new ErrorResponse()
            );
        }

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        UserEntity userEntity = userService.findUserEntityByEmail(userPrinciple.getEmail());

        userService.clearToken(userEntity);

        new SecurityContextLogoutHandler().logout(request,response,authentication);

        ResponseDto<TokenRefreshResponse> responseDto = new ResponseDto<>();
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setErrorCode(ErrorConstant.Code.SUCCESS);
        return responseDto;
    }
}
