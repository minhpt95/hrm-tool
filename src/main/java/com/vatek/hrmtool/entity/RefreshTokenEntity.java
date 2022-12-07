package com.vatek.hrmtool.entity;


import com.vatek.hrmtool.entity.common.CommonEntity;
import com.vatek.hrmtool.entity.common.DateTimeEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity(name = "refresh_token")
@Getter
@Setter
public class RefreshTokenEntity extends CommonEntity {
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity userEntity;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
}
