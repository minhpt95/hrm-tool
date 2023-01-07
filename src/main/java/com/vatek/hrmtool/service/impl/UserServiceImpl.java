package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.constant.CommonConstant;
import com.vatek.hrmtool.constant.ErrorConstant;
import com.vatek.hrmtool.dto.ListResponseDto;
import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.entity.*;
import com.vatek.hrmtool.enumeration.Role;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.jwt.JwtProvider;
import com.vatek.hrmtool.jwt.JwtResponse;
import com.vatek.hrmtool.mapping.UserMapping;
import com.vatek.hrmtool.readable.form.LoginForm;
import com.vatek.hrmtool.readable.form.create.CreateUserForm;
import com.vatek.hrmtool.readable.form.update.UpdateUserForm;
import com.vatek.hrmtool.readable.form.update.UpdateUserRoleForm;
import com.vatek.hrmtool.readable.request.ChangePasswordReq;
import com.vatek.hrmtool.readable.request.ChangeStatusAccountReq;
import com.vatek.hrmtool.respository.*;
import com.vatek.hrmtool.security.service.UserPrinciple;
import com.vatek.hrmtool.service.*;
import com.vatek.hrmtool.util.CommonUtil;
import com.vatek.hrmtool.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.io.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.vatek.hrmtool.util.EmailValidateUtil.isAddressValid;


@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {
    final PasswordEncoder passwordEncoder;
    final UserRepository userRepository;
    final ProjectRepository projectRepository;
    final AuthenticationManager authenticationManager;
    final MailService mailService;
    final JwtProvider jwtProvider;
    final Environment env;
    final RoleRepository roleRepository;
    final RefreshTokenService refreshTokenService;
    private final UserMapping userMapping;
    private final ExcelTemplateServiceImpl excelTemplateService;

    private UploadImageService uploadImageService;

    private final EntityManager entityManager;
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

        Specification<UserEntity> specification = (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.isNotNull(root.get("accessToken")),
                        criteriaBuilder.isTrue(root.get("tokenStatus"))
                );

        List<UserEntity> userEntities = userRepository.findAll(specification);

        if(userEntities.isEmpty()){
            return;
        }

        userEntities.forEach(x -> {
            x.setTokenStatus(false);
            x.setAccessToken(null);
        });

        userRepository.saveAll(userEntities);
    }

    @Override
    public UserEntity findUserEntityByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    @Transactional
    public UserDto findUserEntityById(Long id){

        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new ProductException(
                ErrorResponse
                        .builder()
                        .code(ErrorConstant.Code.NOT_FOUND)
                        .type(ErrorConstant.Type.NOT_FOUND)
                        .message(String.format(ErrorConstant.Message.NOT_FOUND,"User id " + id))
                        .build()
                ));


        return userMapping.toDto(userEntity);
    }



    @Override
    @Transactional
    public ListResponseDto<UserDto> getUserList(Pageable pageable) {
        Page<UserEntity> userEntityPage = userRepository.findAll(CommonUtil.buildPageable(pageable.getPageNumber(), pageable.getPageSize()));
        Page<UserDto> userDtoPage = userEntityPage.map(userMapping::toDto);
        return new ListResponseDto<>(userDtoPage, pageable.getPageSize(), pageable.getPageNumber());
    }

    @Override
    @Transactional
    public UserDto createUser(CreateUserForm form) {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<UserEntity> userEntityList = userRepository.findAllByEmail(form.getEmail());
        if (userEntityList.isPresent()) {
            throw new ProductException(
                    new ErrorResponse(
                            ErrorConstant.Code.ALREADY_EXISTS,
                            ErrorConstant.Type.FAILURE,
                            String.format(ErrorConstant.Message.ALREADY_EXISTS, form.getEmail())
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
        userEntity.setCreatedTime(DateUtil.getInstantNow());
        userEntity.setCreatedBy(userPrinciple.getId());
        userEntity.setModifiedTime(DateUtil.getInstantNow());
        userEntity.setModifiedBy(userPrinciple.getId());

        if(form.getAvatarImage() != null){
            entityManager.persist(userEntity);
            String imageUrl = uploadImageService.uploadAvatarImage(form.getAvatarImage(),userEntity);
            userEntity.setAvatarUrl(imageUrl);
        }

        Collection<RoleEntity> roleEntity = roleRepository.findByRoleIn(form.getRoles());
        userEntity.setRoles(roleEntity);
        userEntity = userRepository.save(userEntity);
        mailService.sendActivationEmail(userEntity);

        return userMapping.toDto(userEntity);
    }

    @Override
    public void activateEmail(Long id, Instant currentTime) {
        var userEntity = userRepository.findUserEntityById(id);
        if (userEntity == null)
        {
            throw new ProductException(
                    new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    String.format(ErrorConstant.Message.NOT_FOUND, id),
                    ErrorConstant.Type.FAILURE)
            );
        }
        var timeCreate = userEntity.getCreatedTime();

        var timeExpired = timeCreate.plus(7, ChronoUnit.DAYS);

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

        var userEntity = userRepository.findUserEntityByEmail(email);
        if (userEntity == null) {

            log.error("User with email not found in database => {}",() -> email);

            throw new ProductException(
                    ErrorResponse
                            .builder()
                            .code(ErrorConstant.Code.NOT_FOUND)
                            .message(String.format(ErrorConstant.Message.NOT_FOUND, email))
                            .type(ErrorConstant.Type.NOT_FOUND)
                            .build()
            );
        }

        var pwd = RandomStringUtils.random(12, CommonConstant.CHARACTERS);

        log.info("start sendEmail forgotPassword()");

        userEntity.setPassword(passwordEncoder.encode(pwd));
        userRepository.save(userEntity);

        mailService.sendForgotEmail(userEntity,pwd);

    }

    @Override
    public void changePassword(ChangePasswordReq changePasswordReq) {
        UserEntity userEntity = userRepository.findUserEntityById(changePasswordReq.getId());
        if (userEntity == null) {
            throw new ProductException(
                    new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    String.format(ErrorConstant.Message.NOT_FOUND, changePasswordReq.getId()),
                    ErrorConstant.Type.NOT_FOUND)
            );
        }
        String pwd = changePasswordReq.getPassword();
        userEntity.setPassword(passwordEncoder.encode(pwd));
        userRepository.save(userEntity);
    }

    @Override
    public Boolean deactivateAccount(ChangeStatusAccountReq changeStatusAccountReq) {
        UserEntity userEntity = userRepository.findUserEntityById(changeStatusAccountReq.getId());

        log.info("deactivate account user by {}",changeStatusAccountReq.getId());

        if (userEntity == null) {

            throw new ProductException(new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    String.format(ErrorConstant.Message.NOT_FOUND, changeStatusAccountReq.getId()),
                    ErrorConstant.Type.NOT_FOUND));

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
    public List<UserDto> getUsersByProjectId(Long projectId) {
        ProjectEntity projectEntity = projectRepository.findById(projectId).orElse(null);

        var userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(projectEntity == null){
            throw new ProductException(
                    ErrorResponse
                            .builder()
                            .type(ErrorConstant.Type.NOT_FOUND)
                            .code(ErrorConstant.Code.NOT_FOUND)
                            .message(String.format(ErrorConstant.Message.NOT_FOUND,"ProjectId " + projectId))
                            .build()
            );
        }

        if(
                !Objects.equals(projectEntity.getId(), userPrinciple.getId()) &&
                        userPrinciple.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals(Role.ADMIN.getAuthority()))
        ){
            throw new AccessDeniedException("Cannot access to another project");
        }

        Collection<UserEntity> userEntityList = projectEntity.getMembers();

        return (List<UserDto>) userMapping.toListDto(userEntityList);
    }

    @Override
    public UserDto changeRole(UpdateUserRoleForm updateUserRoleForm) {
        UserEntity userEntity = userRepository.findUserEntityById(updateUserRoleForm.getId());

        if (userEntity == null) {
            throw new ProductException(new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    String.format(ErrorConstant.Message.NOT_FOUND, updateUserRoleForm.getId()),
                    ErrorConstant.Type.NOT_FOUND));
        }

        RoleEntity role = roleRepository.findByRole(updateUserRoleForm.getRole());

        userEntity.getRoles().clear();

        userEntity.getRoles().add(role);

        userEntity = userRepository.save(userEntity);

        return userMapping.toDto(userEntity);
    }

    @Override
    public UserDto updateUser(UpdateUserForm form) {
        UserEntity userEntity = userRepository.findUserEntityById(form.getId());

        log.info("Update information user by {}",form.getId());


        if (userEntity == null) {
            throw new ProductException(new ErrorResponse(ErrorConstant.Code.NOT_FOUND,
                    String.format(ErrorConstant.Message.NOT_FOUND, form.getId()),
                    ErrorConstant.Type.NOT_FOUND));
        }

        if(StringUtils.isNotBlank(form.getEmail())){
            userEntity.setEmail(form.getEmail());
        }

        if(StringUtils.isNotBlank(form.getName())){
            userEntity.setName(form.getName());
        }

        if(StringUtils.isNotBlank(form.getIdentityCard())){
            userEntity.setIdentityCard(form.getIdentityCard());
        }

        if(StringUtils.isNotBlank(form.getPhoneNumber1())){
            userEntity.setPhoneNumber1(form.getPhoneNumber1());
        }

        if(StringUtils.isNotBlank(form.getCurrentAddress())){
            userEntity.setCurrentAddress(form.getCurrentAddress());
        }

        if(StringUtils.isNotBlank(form.getPermanentAddress())){
            userEntity.setPermanentAddress(form.getPermanentAddress());
        }

        userEntity = userRepository.save(userEntity);
        return userMapping.toDto(userEntity);
    }

    @Override
    @Transactional
    public byte[] exportAllUsersToExcels() {

        List<UserDto> userDtoList = userRepository.findAll().stream().map(userMapping::toDto).toList();

        Map<String,Object> contextMap = Map.of("users",userDtoList);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        excelTemplateService.createDocument(outputStream,"users-template",contextMap);

        return outputStream.toByteArray();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException("Cannot logout");
        }

        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        UserEntity userEntity = findUserEntityByEmail(userPrinciple.getEmail());

        clearToken(userEntity);

        new SecurityContextLogoutHandler().logout(request,response,authentication);
    }
}
