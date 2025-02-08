export class Book {
	state: string;
	available: boolean;
	self: string;
	owner: string;
	bookModel: string;
	images: string[];

	constructor(data: any) {
		this.state = data.state;
		this.available = data.available;
		this.self = data.self;
		this.owner = data.owner;
		this.bookModel = data.bookModel;
		this.images = data.images;
	}
}