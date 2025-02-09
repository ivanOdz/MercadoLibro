import { Component, Input } from '@angular/core';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { Book } from './../../core/models/book.model';
import { BookCardComponent } from './../../shared/components/book-card/book-card.component';

@Component({
  selector: 'app-book-home',
  templateUrl: './book-home.component.html',
  styleUrl: './book-home.component.css',
  standalone: true,
  imports: [ NavbarComponent, BookCardComponent ]
})
export class BookHomeComponent {
	
	books: Book[] = [
		new Book({
			state: 'Nuevo',
			available: true,
			self: '/api/books/1',
			owner: 'Juan Pérez',
			bookModel: 'El Hobbit',
			images: ['']
		})
	];

}
