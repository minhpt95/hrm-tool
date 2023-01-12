package com.vatek.hrmtool.dto.user;

import com.vatek.hrmtool.dto.timesheet.TimesheetExcelDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserExcelDto {
    private String name;
    private List<TimesheetExcelDto> timeSheets = new ArrayList<>();
}
