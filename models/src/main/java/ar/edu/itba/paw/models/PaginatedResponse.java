package ar.edu.itba.paw.models;

import ar.edu.itba.paw.models.utils.pagination.Metadata;

import java.util.List;

public class PaginatedResponse<T, P extends Metadata> {
    private final List<T> data;
    private final P metadata;

    public PaginatedResponse(List<T> data, P metadata) {
        this.data = data;
        this.metadata = metadata;
    }

    public List<T> getData() {
        return data;
    }

    public P getMetadata() {
        return metadata;
    }
}
