export class Review {
  description: string;
  reviewDate: string; // Date
  rating: number;
  self: string;
  subject: string;
  reviewer: string;
  exchange: string;

  constructor(data: any) {
    this.description = data.description;
    this.reviewDate = data.reviewDate;
    this.rating = data.rating;
    this.self = data.self;
    this.subject = data.subject;
    this.reviewer = data.reviewer;
    this.exchange = data.exchange;
  }
}
