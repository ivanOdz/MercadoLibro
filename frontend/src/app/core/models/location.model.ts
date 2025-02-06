export class Location {
  location: string;
  publications: string;
  self: string;

  constructor(data: any) {
    this.location = data.location;
    this.publications = data.image;
    this.self = data.self;
  }
}
