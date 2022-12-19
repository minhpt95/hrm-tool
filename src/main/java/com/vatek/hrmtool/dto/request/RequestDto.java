package com.vatek.hrmtool.dto.request;

import com.vatek.hrmtool.enumeration.RequestStatus;
import com.vatek.hrmtool.enumeration.TypeRequest;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;

public class RequestDto {

    private String id;

    private String requestTitle;

    private String requestReason;

    private RequestStatus status;

    private Instant fromDate;

    private Instant toDate;

    private Boolean isMultipleDay;

    private TypeRequest typeRequest;
}
