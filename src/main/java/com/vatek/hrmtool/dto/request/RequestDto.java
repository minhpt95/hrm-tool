package com.vatek.hrmtool.dto.request;

import com.vatek.hrmtool.enumeration.RequestStatus;
import com.vatek.hrmtool.enumeration.TypeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private Long id;

    private String requestTitle;

    private String requestReason;

    private RequestStatus status;

    private Instant fromDate;

    private Instant toDate;

    private Boolean isMultipleDay;

    private TypeRequest typeRequest;
}
