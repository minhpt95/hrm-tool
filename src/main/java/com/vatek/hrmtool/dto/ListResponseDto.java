package com.vatek.hrmtool.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListResponseDto<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -3931471505590865499L;

    private int pageSize;
    private List<T> items;
    private int pageIndex;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private int pageCount;
    private long totalItemCount;


    public ListResponseDto(Page<T> page,int pageIndex,int pageSize){
        this.items = page.getContent();
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.hasNextPage = page.hasNext();
        this.hasPreviousPage = page.hasPrevious();
        this.pageCount = page.getTotalPages();
        this.totalItemCount = page.getTotalElements();
    }


}
