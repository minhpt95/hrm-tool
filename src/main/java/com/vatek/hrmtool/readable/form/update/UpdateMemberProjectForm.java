package com.vatek.hrmtool.readable.form.update;

import com.vatek.hrmtool.dto.ModifyListDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMemberProjectForm {
    private Long id;

    private List<ModifyListDto> member;
}
