package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.entity.RefreshTokenEntity;
import com.vatek.hrmtool.exception.TokenRefreshException;
import com.vatek.hrmtool.jwt.JwtProvider;
import com.vatek.hrmtool.jwt.payload.request.TokenRefreshRequest;
import com.vatek.hrmtool.jwt.payload.response.TokenRefreshResponse;
import com.vatek.hrmtool.service.RefreshTokenService;
import com.vatek.hrmtool.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/refreshToken/")
public class RefreshTokenController {
    final
    RefreshTokenService refreshTokenService;

    @PostMapping()
    public ResponseDto<?> refreshToken(
            @RequestBody TokenRefreshRequest tokenRefreshRequest,
            HttpServletResponse response
    ) {
        var responseDto = new ResponseDto<>();
        responseDto.setContent(refreshTokenService.refreshToken(tokenRefreshRequest));
        responseDto.setErrorCode(ErrorConstant.Code.SUCCESS);
        responseDto.setErrorType(ErrorConstant.Type.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        return responseDto;
    }
}
