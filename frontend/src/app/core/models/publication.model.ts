export class Publication {
    book: string;
    publicationState: string;
    publicationDatetime: Date;
    locations: string;
    user: string;
    favoriteEndpoint: string;
    self: string

    constructor(data: any) {
        this.book = data.book;
        this.publicationState = data.publicationState;
        this.publicationDatetime = data.publicationDatetime;
        this.locations = data.locations;
        this.user = data.user;
        this.favoriteEndpoint = data.favoriteEndpoint;
        this.self = data.self;
    }
}
