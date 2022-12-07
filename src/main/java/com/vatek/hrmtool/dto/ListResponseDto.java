package com.vatek.hrmtool.dto;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public class ListResponseDto<T> implements Serializable {

    private static final long serialVersionUID = -3931471505590865499L;

    private int pageSize;
    private List<T> items;
    private int pageIndex;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
    private int pageCount;
    private long totalItemCount;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public long getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(long totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public ListResponseDto<T> buildResponseList(Page<T> page, int pageIndex, int pageSize) {
        ListResponseDto<T> result = new ListResponseDto<T>();
        result.setPageSize(pageSize);
        result.setItems(page.getContent());
        result.setPageIndex(pageIndex);
        result.setHasNextPage(page.hasNext());
        result.setHasPreviousPage(page.hasPrevious());
        result.setPageCount(page.getTotalPages());
        result.setTotalItemCount(page.getTotalElements());
        return result;
    }



}
