import {Component, Input} from '@angular/core';
import {BookModel} from "../../../core/models/bookModel.model";
import {LowerCasePipe, NgForOf, NgIf} from "@angular/common";
import {TranslatePipe} from "@ngx-translate/core";
import {BookCardComponent} from "../book-card/book-card.component";

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
    return this.bookModel.coverUri ? this.bookModel.coverUri : this.defaultImage;
  }
}
