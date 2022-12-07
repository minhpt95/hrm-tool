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

    final
    UserService userService;

    final
    JwtProvider jwtProvider;

    @PostMapping()
    public ResponseDto<?> refreshToken(
            @RequestBody TokenRefreshRequest tokenRefreshRequest,
            HttpServletResponse response
    ) {
        String requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenEntity::getUserEntity)
                .map(user -> {
                    String token = jwtProvider.generateTokenFromEmail(user.getEmail());
                    userService.saveToken(token,user);
                    ResponseDto<TokenRefreshResponse> responseDto = new ResponseDto<>();
                    responseDto.setMessage(ErrorConstant.Message.SUCCESS);
                    responseDto.setErrorCode(ErrorConstant.Code.SUCCESS);
                    responseDto.setContent(new TokenRefreshResponse(token, requestRefreshToken));
                    return responseDto;
                })
                .orElseThrow
                        (
                                () ->
                                        new TokenRefreshException
                                                (
                                                        requestRefreshToken,
                                                        "Refresh token is not in database!"
                                                )
                        );
    }

    @GetMapping("/getResponse")
    public ResponseDto<?> getResponse(){
        return new ResponseDto<>();
    }
}
