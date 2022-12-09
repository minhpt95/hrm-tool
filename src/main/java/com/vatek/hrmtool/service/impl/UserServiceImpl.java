package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.CommonConstant;
import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.entity.PrivilegeEntity;
import com.vatek.hrmtool.entity.RefreshTokenEntity;
import com.vatek.hrmtool.entity.RoleEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.entity.enumeration.Privilege;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.jwt.JwtProvider;
import com.vatek.hrmtool.jwt.JwtResponse;
import com.vatek.hrmtool.mapping.UserMapping;
import com.vatek.hrmtool.readable.form.LoginForm;
import com.vatek.hrmtool.readable.form.createForm.CreateUserForm;
import com.vatek.hrmtool.readable.form.updateForm.UpdateUserForm;
import com.vatek.hrmtool.readable.request.ChangePasswordReq;
import com.vatek.hrmtool.readable.request.ChangeStatusAccountReq;
import com.vatek.hrmtool.respository.PrivilegeRepository;
import com.vatek.hrmtool.respository.RoleRepository;
import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.MailService;
import com.vatek.hrmtool.service.RefreshTokenService;
import com.vatek.hrmtool.service.UserService;
import com.vatek.hrmtool.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.vatek.hrmtool.util.EmailValidateUtil.isAddressValid;


@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    final
    PasswordEncoder passwordEncoder;

    final
    UserRepository userRepository;

    final AuthenticationManager authenticationManager;

    final MailService mailService;

    final JwtProvider jwtProvider;

    final Environment env;

    final RoleRepository roleRepository;

    final RefreshTokenService refreshTokenService;

    private final UserMapping userMapping;

    @Override
    public void saveToken(String token, UserEntity userEntity) {
        userEntity.setAccessToken(token);
        userEntity.setTokenStatus(true);
        userRepository.saveAndFlush(userEntity);
    }

    @Override
    public void clearToken(UserEntity userEntity) {
        userEntity.setAccessToken(null);
        userEntity.setTokenStatus(false);
        userRepository.save(userEntity);
    }

    @Override
    public void clearAllToken() {
        List<UserEntity> userEntities = userRepository.findAll().stream().peek(x -> {
            x.setTokenStatus(false);
            x.setAccessToken(null);
        }).collect(Collectors.toList());

        if(userEntities.isEmpty()){
            return;
        }

        userRepository.saveAll(userEntities);
    }

    @Override
    public UserEntity findUserEntityByEmailForLogin(String email) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);

        if (optionalUserEntity.isEmpty())
        {
            throw new ProductException(new ErrorResponse(
                    ErrorConstant.Code.LOGIN_INVALID,
                    ErrorConstant.Type.LOGIN_INVALID,
                    ErrorConstant.Message.LOGIN_INVALID
            ));
        }
        return optionalUserEntity.get();
    }


    @Override
    public UserEntity findUserEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }



    @Override
    public ListResponseDto<UserDto> getUserList(int pageIndex, int pageSize) {
        Page<UserEntity> page = userRepository.findAll(CommonUtil.buildPageable(pageIndex, pageSize));
        Page<UserDto> userDtoPage = page.map(userMapping::toDto);
        ListResponseDto<UserDto> result = new ListResponseDto<>();
        return result.buildResponseList(userDtoPage, pageSize, pageIndex);
    }

    @Override
    @Transactional
    @SneakyThrows
    public UserDto createUser(CreateUserForm form) {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<UserEntity> userEntityList = userRepository.findAllByEmail(form.getEmail());
        if (userEntityList.isPresent()) {
            throw new ProductException(
                    new ErrorResponse(
                            ErrorConstant.Code.ALREADY_EXISTS,
                            ErrorConstant.Type.FAILURE,
                            String.format(ErrorConstant.Message.ALREADY_EXISTS,form.getEmail())
                    )
            );
        }
        UserEntity userEntity = new UserEntity();

        userEntity.setCurrentAddress(form.getCurrentAddress());

        userEntity.setEmail(form.getEmail());
        userEntity.setIdentityCard(form.getIdentityCard());
        userEntity.setEnabled(false);
        userEntity.setName(form.getName());
        userEntity.setPassword(passwordEncoder.encode(form.getPassword()));
        userEntity.setPermanentAddress(form.getPermanentAddress());
        userEntity.setPhoneNumber1(form.getPhoneNumber1());

        Collection<RoleEntity> roleEntity = roleRepository.findByRoleIn(form.getRoles());
        userEntity.setRoles(roleEntity);

        userEntity.setCreatedTime(Instant.now());
        userEntity.setCreatedBy(userPrinciple.getId());
        userEntity.setModifiedTime(Instant.now());
        userEntity.setModifiedBy(userPrinciple.getId());

        userEntity = userRepository.save(userEntity);
        mailService.sendActivationEmail(userEntity);

        return userMapping.toDto(userEntity);
    }

    @Override
    public void activateEmail(Long id, Instant currentTime) {
        UserEntity userEntity = userRepository.findUserEntityById(id);
        if (userEntity == null)
        {
            throw new ProductException(
                    new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    String.format(ErrorConstant.Message.NOT_EXISTS, id),
                    ErrorConstant.Type.FAILURE)
            );
        }
        Instant timeCreate = userEntity.getCreatedTime();

        Instant timeExpired = timeCreate.plus(7, ChronoUnit.DAYS);

        if (timeExpired.isBefore(currentTime)) {
            throw new ProductException(
                    new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    ErrorConstant.Message.END_OF_TIME,
                    ErrorConstant.Type.FAILURE)
            );
        }
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
    }

    @Override
    @SneakyThrows
    public void forgotPassword(String email) {

        var isValidEmail = isAddressValid(email);

        if(!isValidEmail){
            log.error("email not valid => {}",() -> email);
            throw new ProductException(
                new ErrorResponse()
            );
        }

        UserEntity userEntity = userRepository.findUserEntityByEmail(email);
        if (userEntity == null) {

            log.error("User with email not found in database => {}",() -> email);

            throw new ProductException(new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    String.format(ErrorConstant.Message.NOT_EXISTS, email),
                    ErrorConstant.Type.FAILURE));
        }

        String pwd = RandomStringUtils.random(12, CommonConstant.CHARACTERS);

        log.info("start sendEmail forgotPassword()");

        userEntity.setPassword(passwordEncoder.encode(pwd));
        userRepository.save(userEntity);

        mailService.sendForgotEmail(userEntity,pwd);

    }

    @Override
    public Boolean changePassword(ChangePasswordReq changePasswordReq) {
        UserEntity userEntity = userRepository.findUserEntityById(changePasswordReq.getId());
        if (userEntity == null) {
            throw new ProductException(
                    new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    String.format(ErrorConstant.Message.NOT_EXISTS, changePasswordReq.getId()),
                    ErrorConstant.Type.FAILURE)
            );
        }
        String pwd = changePasswordReq.getPassword();
        userEntity.setPassword(passwordEncoder.encode(pwd));
        userRepository.save(userEntity);
        return true;
    }

    @Override
    public Boolean deactivateAccount(ChangeStatusAccountReq changeStatusAccountReq) {
        UserEntity userEntity = userRepository.findUserEntityById(changeStatusAccountReq.getId());

        log.info("deactivate account user by {}",changeStatusAccountReq.getId());

        if (userEntity == null) {

            throw new ProductException(new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    String.format(ErrorConstant.Message.NOT_EXISTS, changeStatusAccountReq.getId()),
                    ErrorConstant.Type.FAILURE));

        }

        userEntity.setEnabled(false);
        userRepository.save(userEntity);
        return true;
    }

    @Override
    public JwtResponse authenticateUser(LoginForm loginForm) {

        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(loginForm.getEmail());

        UserEntity userEntity = optionalUserEntity.orElseThrow(() -> {
            throw new ProductException(new ErrorResponse(
                    ErrorConstant.Code.LOGIN_INVALID,
                    ErrorConstant.Type.LOGIN_INVALID,
                    ErrorConstant.Message.LOGIN_INVALID
            ));
        });

        if(!passwordEncoder.matches(loginForm.getPassword(),userEntity.getPassword())){
            throw new ProductException(new ErrorResponse(
                    ErrorConstant.Code.LOGIN_INVALID,
                    ErrorConstant.Type.LOGIN_INVALID,
                    ErrorConstant.Message.LOGIN_INVALID
            ));
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getEmail(),
                        loginForm.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        String jwt = jwtProvider.generateJwtToken(authentication);

        saveToken(jwt,userEntity);

        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userEntity);

//        List<String> roles = userPrinciple.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());



        return new JwtResponse(
                jwt,
                refreshToken.getToken(),
                userPrinciple.getId(),
                userEntity.getName(),
                userPrinciple.getEmail(),
                userPrinciple.getRoles().stream().toList(),
                userPrinciple.getPrivileges().stream().toList()
        );
    }

    @Override
    public UserDto updateUser(UpdateUserForm form) {
        UserEntity userEntity = userRepository.findUserEntityById(form.getId());

        log.info("update information user by {}",form.getId());


        if (userEntity == null) {
            throw new ProductException(new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    String.format(ErrorConstant.Message.NOT_EXISTS, form.getId()),
                    ErrorConstant.Type.FAILURE));
        }
        userEntity.setEmail(form.getEmail());
        userEntity.setName(form.getName());
        userEntity.setIdentityCard(form.getIdentityCard());
        userEntity.setPhoneNumber1(form.getPhoneNumber1());
//        userEntity.setPhoneNumber2(form.getPhoneNumber2());
        userEntity.setCurrentAddress(form.getCurrentAddress());
        userEntity.setPermanentAddress(form.getPermanentAddress());
//        userEntity.setDescription(form.getDescription());

        userEntity = userRepository.save(userEntity);
        return userMapping.toDto(userEntity);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new ProductException(
                    new ErrorResponse()
            );
        }

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        UserEntity userEntity = findUserEntityByEmail(userPrinciple.getEmail());

        clearToken(userEntity);

        new SecurityContextLogoutHandler().logout(request,response,authentication);
    }
}
