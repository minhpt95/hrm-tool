package com.vatek.hrmtool.respository;

import com.vatek.hrmtool.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserEntity, Long> , JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByEmail(String email);

    @Override
    boolean existsById(Long id);

    Page<UserEntity> findAll(Pageable pageable);

    Optional<UserEntity> findAllByEmail(String email);

    UserEntity findUserEntityById(Long id);

    UserEntity findUserEntityByEmail(String email);
}
