package com.vatek.hrmtool.dto;

import com.vatek.hrmtool.enumeration.Action;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyListDto {
    private Long id;
    private Action action;
}
