package com.vatek.hrmtool.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListResponseDto<T> implements Serializable {

    private static final long serialVersionUID = -3931471505590865499L;

    private int pageSize;
    private List<T> items;
    private int pageIndex;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private int pageCount;
    private long totalItemCount;

    public ListResponseDto<T> buildResponseList(Page<T> page, int pageIndex, int pageSize) {
        return ListResponseDto.<T>builder()
                .items(page.getContent())
                .pageSize(pageSize)
                .pageIndex(pageIndex)
                .hasNextPage(page.hasNext())
                .hasPreviousPage(page.hasPrevious())
                .pageCount(page.getTotalPages())
                .totalItemCount(page.getTotalElements())
                .build();
    }



}
