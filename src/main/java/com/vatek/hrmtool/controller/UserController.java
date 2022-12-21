package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.readable.form.createForm.CreateUserForm;
import com.vatek.hrmtool.readable.form.updateForm.UpdateUserForm;
import com.vatek.hrmtool.readable.request.ChangePasswordReq;
import com.vatek.hrmtool.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
@Log4j2
public class UserController {

    final UserService userService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping(value = "/change-password")
    public  ResponseDto<Boolean> changePassword(@Valid @RequestBody ChangePasswordReq changePasswordReq){
        ResponseDto<Boolean> responseDto = new ResponseDto<>();
        
        responseDto.setContent(userService.changePassword(changePasswordReq));
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        
        return responseDto;
    }
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PutMapping(value = "/update")
    public ResponseDto<UserDto> updateUser (@RequestBody UpdateUserForm form) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        responseDto.setContent(userService.updateUser(form));
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);

        return responseDto;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping(value = "/create",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<UserDto> createUser (@ModelAttribute CreateUserForm createUserForm) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();

        responseDto.setContent(userService.createUser(createUserForm));
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);

        return responseDto;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/get-users-pageable")
    public ListResponseDto<UserDto> getUserPageable(Pageable pageable){
        return userService.getUserList(pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping(value = "/role/change")
    public ListResponseDto<UserDto> changeRole(Pageable pageable){
        return userService.getUserList(pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_PM')")
    @GetMapping(value = "/get-users-by-project-id/{projectId}")
    public ResponseDto<List<UserDto>> getUserByProjectId(@PathVariable("projectId") Long projectId){
        ResponseDto<List<UserDto>> responseDto = new ResponseDto<>();
        responseDto.setContent(userService.getUsersByProjectId(projectId));
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);

        return responseDto;
    }

    @PostMapping("/logout")
    public ResponseDto<?> logout(HttpServletRequest request, HttpServletResponse response) {

        userService.logout(request,response);

        ResponseDto<?> responseDto = new ResponseDto<>();
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        return responseDto;
    }
}
