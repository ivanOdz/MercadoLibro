export class User {
    username: string;
    mail: string;
    ratingCount: number;
    ratingAverage: number;
    self: string;
    image: string;
    favoriteLocation: string;
    locations: string;
    reviews: string;
    books: string;
    favorites: string;

    constructor(data: any) {
        this.username = data.username;
        this.mail = data.mail;
        this.ratingCount = data.ratingCount;
        this.ratingAverage = data.ratingAverage;
        this.self = data.self;
        this.image = data.image;
        this.favoriteLocation = data.favoriteLocation;
        this.locations = data.locations;
        this.reviews = data.reviews;
        this.books = data.books;
        this.favorites = data.favorites;
    }
}
