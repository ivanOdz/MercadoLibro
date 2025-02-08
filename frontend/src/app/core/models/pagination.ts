export class Pagination {
    totalAmount: number;
    maxPage: number;
    currentPage: number;

    constructor(total: number, max: number, current: number) {
        this.totalAmount = total;
        this.maxPage = max;
        this.currentPage = current;
    }
}