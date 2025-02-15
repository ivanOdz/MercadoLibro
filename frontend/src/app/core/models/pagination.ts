export class Pagination {
    next: string | undefined;
    prev: string | undefined;
    last: string | undefined;
    first: string | undefined;
	pages: number = 0;
	
    constructor(linksHeader: string | null) {
		this.pages = 0;
        if (linksHeader) {
            const links = linksHeader.split(',').map(link => link.trim());
            links.forEach(link => {
                const match = link.match(/<(.*?)>; rel="(.*?)"/);
                if (match) {
                    const url = match[1];
                    const rel = match[2];
                    if (rel === 'first') this.first = url || undefined;
                    if (rel === 'prev') this.prev = url || undefined;
                    if (rel === 'next') this.next = url || undefined;
					if (rel === 'last') {
					    this.last = url || undefined;
					    const pageMatch = url.match(/[?&]page=(\d+)/);
					    if (pageMatch) {
					        this.pages = parseInt(pageMatch[1], 10);
					    }
					}
                }
            });
        }
    }

    hasPrev(): boolean {
        return !!this.prev;
    }

    hasNext(): boolean {
        return !!this.next;
    }

    multiplePages(): boolean {
        return this.first != this.last;
    }
}