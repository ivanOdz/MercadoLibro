import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookModel } from "../../../core/models/bookModel.model";
import { TranslatePipe } from "@ngx-translate/core";
import { Dialog } from "primeng/dialog";
import { NgClass, NgForOf, NgIf } from "@angular/common";
import { Button } from "primeng/button";
import { DropdownModule } from "primeng/dropdown";
import { AuthService } from "../../../core/services/auth.service";
import { FormsModule } from "@angular/forms";
import {BookService} from "../../../core/services/book.service";
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-book-modal',
  standalone: true,
  imports: [
    TranslatePipe,
    Dialog,
    NgClass,
    NgForOf,
    NgIf,
    Button,
    DropdownModule,
    FormsModule
  ],
  templateUrl: './book-modal.component.html',
  styleUrls: ['./book-modal.component.css', '../../../shared/book-card/book-card.component.css']
})
export class BookModalComponent {
  bookUrl: string = (environment.production ? environment.productionUrl : environment.developmentUrl) + '/books';
  @Input() bookModel!: BookModel;
  @Input() bookImage!: string;
  @Input() modalVisible: boolean = false;
  @Output() modalVisibleChange = new EventEmitter<boolean>();

  bookStates = ['new', 'like.new', 'very.good', 'good', 'acceptable', 'worn'];

  rating: number = 1;
  bookState: string = '';
  images: File[] = [];
  imagePreviews: string[] = [];
  uploadProgress = 0;
  user: string | undefined = '';

  constructor(private authService: AuthService, private bookService: BookService) {}


  openModal() {
    this.modalVisible = true;
    this.modalVisibleChange.emit(this.modalVisible);
  }

  closeModal() {
    this.modalVisible = false;
    this.modalVisibleChange.emit(this.modalVisible);
  }

  getFormattedAuthors(authors: string[]): string {
    return authors.join(', ');
  }

  setRating(star: number) {
    this.rating = star;
  }


  onImagesSelected(event: any) {
    const files: FileList = event.target.files;

    for (let i = 0; i < files.length; i++) {
      this.images.push(files[i]);
      const reader = new FileReader();

      reader.onload = () => {
        this.imagePreviews.push(reader.result as string);
      };

      reader.readAsDataURL(files[i]);
    }
  }

  removeImage(index: number) {
    this.images.splice(index, 1);
    this.imagePreviews.splice(index, 1);
  }

  isFormValid(): boolean {
    return this.rating > 0 && this.bookState.length > 0;
  }

  submitForm() {
    this.authService.loggedUser$.subscribe(user => {
      this.user = user?.self;
    });
    console.log('Datos:', { rating: this.rating, bookState: this.bookState });
    this.closeModal();
    this.bookService.uploadBook(this.bookUrl, this.bookModel.self, this.rating, this.bookState, this.user);
  }
}
