package ar.edu.itba.paw.models.utils.pagination;

import ar.edu.itba.paw.models.utils.Constants;

public abstract class Metadata {
    protected int currentPage;
    protected int maxPage;
    protected int totalResults;

    protected Metadata(int currentPage, int totalResults, int pageSize) {
        this.currentPage = currentPage;
        this.totalResults = totalResults;
        this.maxPage = (int) Math.ceil((double) totalResults/ pageSize) - 1;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
