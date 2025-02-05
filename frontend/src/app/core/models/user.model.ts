export interface User {
    username: string;
    mail: string;
    ratingCount: number;
    ratingAverage: number;
    self: string; // Usamos string para URI
    image: string; // Usamos string para URI
    favoriteLocation: string; // Usamos string para URI
    locations: string; // Usamos string para URI
    reviews: string; // Usamos string para URI
    books: string; // Usamos string para URI
    favorites: string; // Usamos string para URI
}
