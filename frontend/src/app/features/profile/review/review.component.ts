import { Component, Input } from '@angular/core';
import { Review } from '../../../core/models/review.model';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  imports: [
    NgClass
  ],
  styleUrls: ['./review.component.css']
})
export class ReviewComponent {
  @Input() review!: Review;

  get formattedDate(): string {
    return new Date(this.review.reviewDate).toLocaleDateString();
  }

  get starArray(): number[] {
    return Array(5).fill(0).map((_, i) => i + 1);
  }
}
