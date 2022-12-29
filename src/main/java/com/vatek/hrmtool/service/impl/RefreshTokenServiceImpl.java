package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.entity.RefreshTokenEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.exception.TokenRefreshException;
import com.vatek.hrmtool.jwt.JwtProvider;
import com.vatek.hrmtool.jwt.payload.request.TokenRefreshRequest;
import com.vatek.hrmtool.jwt.payload.response.TokenRefreshResponse;
import com.vatek.hrmtool.respository.RefreshTokenRepository;
import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.service.RefreshTokenService;
import com.vatek.hrmtool.service.UserService;
import com.vatek.hrmtool.util.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${hrm.app.refreshTokenExpiration}")
    private Long refreshTokenDurationMs;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtProvider jwtProvider;

    public RefreshTokenServiceImpl(
            RefreshTokenRepository refreshTokenRepository,
            UserRepository userRepository,
            JwtProvider jwtProvider
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest tokenRefreshRequest){
        var requestRefreshToken = tokenRefreshRequest.getRefreshToken();

        return findByToken(requestRefreshToken)
            .map(this::verifyExpiration)
            .map(RefreshTokenEntity::getUserEntity)
            .map(user -> {
                var token = jwtProvider.generateTokenFromEmail(user.getEmail());
                user.setAccessToken(token);
                user.setTokenStatus(true);
                userRepository.saveAndFlush(user);
                return new TokenRefreshResponse(token, requestRefreshToken);
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

    @Override
    public RefreshTokenEntity createRefreshToken(UserEntity userEntity) {
        if(userEntity == null){
            throw new ProductException(new ErrorResponse());
        }

        var refreshToken = refreshTokenRepository.findByUserEntityId(userEntity.getId()).orElse(null);

        if(refreshToken == null){
            refreshToken = new RefreshTokenEntity();
            refreshToken.setUserEntity(userEntity);
            refreshToken.setCreatedBy(userEntity.getId());
            refreshToken.setCreatedTime(DateUtil.getInstantNow());
        }else{
            refreshToken.setModifiedBy(userEntity.getId());
            refreshToken.setModifiedTime(DateUtil.getInstantNow());
        }
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().isBefore(DateUtil.getInstantNow())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Override
    @Transactional
    public Long deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserEntityId(userId);
        return userId;
    }
}
