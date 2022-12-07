package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.jwt.JwtResponse;
import com.vatek.hrmtool.readable.form.LoginForm;
import com.vatek.hrmtool.readable.form.createForm.CreateUserForm;
import com.vatek.hrmtool.readable.form.updateForm.UpdateUserForm;
import com.vatek.hrmtool.readable.request.ChangePasswordReq;
import com.vatek.hrmtool.readable.request.ChangeStatusAccountReq;

import java.time.Instant;

public interface UserService {
    void saveToken(String token, UserEntity userEntity);

    void clearToken(UserEntity userEntity);

    void clearAllToken();

    UserEntity findUserEntityByEmailForLogin(String email);

    UserEntity findUserEntityByEmail(String email);

    ListResponseDto<UserDto> getUserList
    (
            int pageIndex,
            int pageSize
    );
    
    UserDto createUser(CreateUserForm form);

    Boolean activateEmail(Long id, Instant timeOut);

    void forgotPassword(String email);

    Boolean changePassword(ChangePasswordReq changePasswordReq);

    Boolean deactivateAccount(ChangeStatusAccountReq changeStatusAccountReq);

    UserDto updateUser(UpdateUserForm form);

    JwtResponse authenticateUser(LoginForm loginForm);
}
