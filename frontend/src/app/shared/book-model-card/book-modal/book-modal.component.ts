import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BookModel} from "../../../core/models/bookModel.model";
import {FormBuilder, FormGroup, FormsModule, Validators} from "@angular/forms";
import {TranslatePipe} from "@ngx-translate/core";
import {Dialog} from "primeng/dialog";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {Button} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";

@Component({
  selector: 'app-book-modal',
  imports: [
    TranslatePipe,
    Dialog,
    NgClass,
    NgForOf,
    NgIf,
    Button,
    FormsModule,
    DropdownModule
  ],
  templateUrl: './book-modal.component.html',
  styleUrls: ['./book-modal.component.css', '../../../components/book-card/book-card.component.css']
})
export class BookModalComponent {
  @Input() bookModel!: BookModel;
  @Input() bookImage!: string;
  @Input() modalVisible: boolean = false;
  @Output() modalVisibleChange = new EventEmitter<boolean>();
  bookStates = [ 'new', 'like.new', 'very.good', 'good', 'acceptable', 'worn' ];
  bookForm: FormGroup;
  rating: number = 0;
  bookState: string = '';

  constructor(private fb: FormBuilder) {
    this.bookForm = this.fb.group({
      rating: [null, [Validators.required, Validators.min(1), Validators.max(5)]],
      bookState: ['', Validators.required]
    });
  }

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
    this.bookForm.controls['rating'].setValue(star);
  }

  submitForm() {
    if (this.bookForm.valid) {
      console.log('Formulario enviado:', this.bookForm.value);
      this.closeModal();
    } else {
      console.log('Formulario inválido');
    }
  }
}
