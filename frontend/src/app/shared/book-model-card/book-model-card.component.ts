import {Component, Input} from '@angular/core';
import {BookModel} from "../../core/models/bookModel.model";
import {LowerCasePipe, NgForOf, NgIf} from "@angular/common";
import {TranslatePipe} from "@ngx-translate/core";
import {environment} from "../../../environments/environment";
import {Button} from "primeng/button";
import {Dialog} from "primeng/dialog";
import {Divider} from "primeng/divider";
import {Select} from "primeng/select";
import {MatIcon} from "@angular/material/icon";

@Component({
  selector: 'app-book-model-card',
  imports: [
    LowerCasePipe,
    TranslatePipe,
    NgForOf,
    Button,
    Dialog,
    Divider,
    NgIf,
    Select,
    MatIcon
  ],
  templateUrl: './book-model-card.component.html',
  styleUrl: './book-model-card.component.css'
})
export class BookModelCardComponent {

  @Input() bookModel!: BookModel;
  bookImage!: string;
  defaultImage: string = './assets/book.jpg';

    ngOnInit() {
        console.log(this.bookModel);
        this.bookImage = this.getBookImage();
    }

  private getBookImage(): string {
    return this.bookModel.cover ? this.getBaseUrl() + this.bookModel.cover : this.defaultImage;
  }

  getBaseUrl() {
    return `${environment.production? environment.productionUrl  : environment.developmentUrl}`;
  }

  getFormattedAuthors(authors: string[]): string {
    const maxAuthors = 3;
    if (authors.length > maxAuthors) {
      return authors.slice(0, maxAuthors).join(', ') + ', ...';
    }
    return authors.join(', ');
  }

  getStarArray(averageRating: number): string[] {
    const fullStars = Math.floor(averageRating);
    const hasHalfStar = averageRating % 1 !== 0;
    const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

    return [
      ...Array(fullStars).fill('star'),
      ...(hasHalfStar ? ['star_half'] : []),
      ...Array(emptyStars).fill('star_border')
    ];
  }

}
