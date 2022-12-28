package com.vatek.hrmtool.service;

import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.jwt.JwtResponse;
import com.vatek.hrmtool.readable.form.LoginForm;
import com.vatek.hrmtool.readable.form.createForm.CreateUserForm;
import com.vatek.hrmtool.readable.form.updateForm.UpdateUserForm;
import com.vatek.hrmtool.readable.form.updateForm.UpdateUserRoleForm;
import com.vatek.hrmtool.readable.request.ChangePasswordReq;
import com.vatek.hrmtool.readable.request.ChangeStatusAccountReq;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

public interface UserService {
    void saveToken(String token, UserEntity userEntity);

    void clearToken(UserEntity userEntity);

    void clearAllToken();

    UserEntity findUserEntityByEmail(String email);

    @Transactional
    UserDto findUserEntityById(Long id);

    ListResponseDto<UserDto> getUserList
    (
            Pageable pageable
    );
    
    UserDto createUser(CreateUserForm form);

    void activateEmail(Long id, Instant timeOut);

    void forgotPassword(String email);

    void changePassword(ChangePasswordReq changePasswordReq);

    Boolean deactivateAccount(ChangeStatusAccountReq changeStatusAccountReq);

    UserDto updateUser(UpdateUserForm form);

    UserDto changeRole(UpdateUserRoleForm updateUserRoleForm);

    JwtResponse authenticateUser(LoginForm loginForm);

    void logout(HttpServletRequest request, HttpServletResponse response);

    List<UserDto> getUsersByProjectId(Long projectId);

    byte[] exportAllUsersToExcels();
}
