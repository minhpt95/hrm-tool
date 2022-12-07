package com.vatek.hrmtool.controller;

import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ResponseDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.entity.RefreshTokenEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.jwt.JwtProvider;
import com.vatek.hrmtool.jwt.JwtResponse;
import com.vatek.hrmtool.readable.form.LoginForm;
import com.vatek.hrmtool.readable.form.createForm.CreateUserForm;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.MailService;
import com.vatek.hrmtool.service.RefreshTokenService;
import com.vatek.hrmtool.service.UserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/auth")
public class AuthRestController {

  final
  AuthenticationManager authenticationManager;

  final
  MailService mailService;

  final
  UserService userService;

  final
  PasswordEncoder encoder;

  final
  JwtProvider jwtProvider;

  final
  ModelMapper modelMapper;

  final
  RefreshTokenService refreshTokenService;

  @PostMapping(value = "/register",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseDto<UserDto> registerUser(@Valid @ModelAttribute CreateUserForm createUserForm) {
    UserDto createUserDto = userService.createUser(createUserForm);

    ResponseDto<UserDto> responseDto = new ResponseDto<>();
    responseDto.setContent(createUserDto);
    responseDto.setErrorCode(ErrorConstant.Code.SUCCESS);
    responseDto.setErrorType(ErrorConstant.Type.SUCCESS);
    responseDto.setMessage(ErrorConstant.Message.SUCCESS);

    return responseDto;
  }

  @PostMapping("/login")
  public ResponseDto<?> authenticateUser(@RequestBody LoginForm loginForm) {

    ResponseDto<JwtResponse> responseDto = new ResponseDto<>();

    UserEntity user = userService.findUserEntityByEmailForLogin(loginForm.getEmail());

    if(!encoder.matches(loginForm.getPassword(),user.getPassword())){
      throw new ProductException(new ErrorResponse(
          ErrorConstant.Code.LOGIN_INVALID,
          ErrorConstant.Type.LOGIN_INVALID,
          ErrorConstant.Message.LOGIN_INVALID
      ));
    }

    Authentication authentication = authenticationManager.authenticate
        (
            new UsernamePasswordAuthenticationToken(
                loginForm.getEmail(),
                loginForm.getPassword()
            )
        );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

    String jwt = jwtProvider.generateJwtToken(authentication);

    userService.saveToken(jwt,user);

    RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(user);

    List<String> roles = userPrinciple.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

    responseDto.setErrorCode(ErrorConstant.Code.SUCCESS);
    responseDto.setErrorType(ErrorConstant.Type.SUCCESS);
    responseDto.setMessage(ErrorConstant.Message.SUCCESS);
    responseDto.setContent(
        new JwtResponse(
            jwt,
            refreshToken.getToken(),
            userPrinciple.getId(),
            userPrinciple.getUsername(),
            userPrinciple.getEmail(),
            roles
        )
    );
    return responseDto;
  }

  @SneakyThrows
  @PostMapping("/forgotPassword")
  public ResponseDto<?> forgotPassword(@RequestParam(name = "email",defaultValue = "") String email) {

    if (StringUtils.isBlank(email)) {
      log.error("parameter email empty => {}",() -> email);
      throw new ProductException(
          new ErrorResponse()
      );
    }

    userService.forgotPassword(email);

    ResponseDto<?> responseDto = new ResponseDto<>();
    responseDto.setMessage(ErrorConstant.Code.SUCCESS);
    responseDto.setErrorCode(ErrorConstant.Code.SUCCESS);
    return responseDto;
  }


}
