package ar.edu.itba.paw.models;

import java.util.List;

public class PaginatedResponse<T> {
    private final List<T> data;
    private final PageInfo pageInfo;

    public PaginatedResponse(List<T> data, PageInfo pageInfo) {
        this.data = data;
        this.pageInfo = pageInfo;
    }

    public List<T> getData() {
        return data;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }
}
