package com.vatek.hrmtool.readable.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonPageRequest<T> {
    private int pageIndex;
    private int pageSize;
    private List<String> sortBy;
    private String sortDirection;
    private T filterObject;
}
