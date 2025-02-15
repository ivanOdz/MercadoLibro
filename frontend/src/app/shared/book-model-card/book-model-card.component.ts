import {Component, Input} from '@angular/core';
import {BookModel} from "../../core/models/bookModel.model";
import {LowerCasePipe, NgForOf, NgIf} from "@angular/common";
import {TranslatePipe} from "@ngx-translate/core";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-book-model-card',
  imports: [
    LowerCasePipe,
    TranslatePipe,
    NgForOf
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
}
