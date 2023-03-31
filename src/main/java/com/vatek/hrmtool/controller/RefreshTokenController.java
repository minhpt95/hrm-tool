package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.jwt.payload.request.TokenRefreshRequest;
import com.vatek.hrmtool.jwt.payload.response.TokenRefreshResponse;
import com.vatek.hrmtool.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/refresh-token")
public class RefreshTokenController {
    final
    RefreshTokenService refreshTokenService;

    @PostMapping()
    public ResponseDto<TokenRefreshResponse> refreshToken(
            @RequestBody TokenRefreshRequest tokenRefreshRequest
    ) {
        var responseDto = new ResponseDto<TokenRefreshResponse>();
        responseDto.setContent(refreshTokenService.refreshToken(tokenRefreshRequest));
        responseDto.setCode(ErrorConstant.Code.SUCCESS);
        responseDto.setType(ErrorConstant.Type.SUCCESS);
        responseDto.setMessage(ErrorConstant.Message.SUCCESS);
        return responseDto;
    }
}
