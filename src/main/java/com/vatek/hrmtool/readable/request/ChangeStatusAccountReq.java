package com.vatek.hrmtool.readable.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusAccountReq {
    private Long id;
    private boolean status;
}
