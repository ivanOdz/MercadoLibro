package ar.edu.itba.paw.models.utils.pagination;

public abstract class Metadata {
    protected int currentPage;
    protected int maxPage;
    protected int totalResults;

    protected Metadata(int currentPage, int maxPage, int totalResults) {
        this.currentPage = currentPage;
        this.maxPage = maxPage;
        this.totalResults = totalResults;
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

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}
