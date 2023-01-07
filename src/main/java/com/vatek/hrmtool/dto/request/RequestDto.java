package com.vatek.hrmtool.dto.request;

import com.vatek.hrmtool.dto.user.UserDto;
import com.vatek.hrmtool.enumeration.ApprovalStatus;
import com.vatek.hrmtool.enumeration.TypeRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long id;
    private String requestTitle;
    private String requestReason;
    private ApprovalStatus status;
    private List<DayOffGroup> dayOffGroupList;
    private TypeRequest typeRequest;
    private UserDto requester;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DayOffGroup {
        private Instant fromDate;
        private Instant toDate;
    }
}
