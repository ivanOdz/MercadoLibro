export class BookModel {
	isbn: string;
	title: string;
	editorial: string;
	description?: string;
	genre: string;
	edition: number;
	weight: number;
	pages: number;
	bookLanguage: string;
	dimension: string;
	publicationYear: number;
	isPocketEdition: boolean;
	isHardcover: boolean;
	ratingCount: number;
	averageRating: number;
	authors: string[];
	coverUri: string;
	selfUri: string;
	
	constructor(data: any) {
		this.isbn = data.isbn;
		this.title = data.title;
		this.editorial = data.editorial;
		this.description = data.description;
		this.genre = data.genre;
		this.edition = data.edition;
		this.weight = data.weight;
		this.pages = data.pages;
		this.bookLanguage = data.bookLanguage;
		this.dimension = data.dimension;
		this.publicationYear = data.publicationYear;
		this.isPocketEdition = data.isPocketEdition;
		this.isHardcover = data.isHardcover;
		this.ratingCount = data.ratingCount;
		this.averageRating = data.averageRating;
		this.authors = data.authors;
		this.coverUri = data.coverUri;
		this.selfUri = data.selfUri;
	}
}