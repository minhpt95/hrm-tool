package com.vatek.hrmtool.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateTimeEntity {
    @Column(nullable = false)
    private Instant createdTime;

    @Column(nullable = false)
    private Long createdBy;

    @Column
    private Instant modifiedTime;

    @Column
    private Long modifiedBy;
}
