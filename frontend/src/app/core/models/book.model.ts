export class Book {
	state: string;
	isAvailable: boolean;
	selfUri: string;
	ownerUri: string;
	bookModelUri: string;
	imagesUri: string[];
	
	constructor(data: any) {
		this.state = data.state;
		this.isAvailable = data.isAvailable;
		this.selfUri = data.selfUri;
		this.ownerUri = data.ownerUri;
		this.bookModelUri = data.bookModelUri;
		this.imagesUri = data.imagesUri;
	}
}