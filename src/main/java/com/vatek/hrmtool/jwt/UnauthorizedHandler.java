package com.vatek.hrmtool.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.exception.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.Normalizer;

@Component
@Log4j2
@AllArgsConstructor
public class UnauthorizedHandler implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException e)
            throws IOException {

        log.error("Unauthorized error. Message - {}", e.getMessage());

        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .code(ErrorConstant.Code.UNAUTHORIZED)
                .type(ErrorConstant.Type.UNAUTHORIZED)
                .message(e.getMessage())
                .build();

        String errorResponseString = objectMapper.writeValueAsString(errorResponse);

        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(errorResponseString);
    }
}
