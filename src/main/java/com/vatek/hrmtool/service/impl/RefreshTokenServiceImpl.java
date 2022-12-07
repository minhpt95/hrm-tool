package com.vatek.hrmtool.service.impl;

import com.vatek.hrmtool.entity.RefreshTokenEntity;
import com.vatek.hrmtool.entity.UserEntity;
import com.vatek.hrmtool.exception.ErrorResponse;
import com.vatek.hrmtool.exception.ProductException;
import com.vatek.hrmtool.exception.TokenRefreshException;
import com.vatek.hrmtool.respository.RefreshTokenRepository;
import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.service.RefreshTokenService;
import com.vatek.hrmtool.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${highschool.app.refreshTokenExpiration}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;



    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenEntity createRefreshToken(UserEntity userEntity) {
        if(userEntity == null){
            throw new ProductException(new ErrorResponse());
        }

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();

        refreshToken.setUserEntity(userEntity);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedBy(userEntity.getId());
        refreshToken.setCreatedTime(DateUtil.getInstantNow());
        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) <= 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Override
    @Transactional
    public Long deleteByUserId(Long userId) {
        refreshTokenRepository.deleteById(userId);
        return userId;
    }
}
