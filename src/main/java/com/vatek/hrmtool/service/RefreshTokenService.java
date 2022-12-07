package com.vatek.hrmtool.service;


import com.vatek.hrmtool.entity.RefreshTokenEntity;
import com.vatek.hrmtool.entity.UserEntity;

import javax.transaction.Transactional;
import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshTokenEntity> findByToken(String token);

    RefreshTokenEntity createRefreshToken(UserEntity userEntity);

    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);

    @Transactional
    Long deleteByUserId(Long userId);
}
