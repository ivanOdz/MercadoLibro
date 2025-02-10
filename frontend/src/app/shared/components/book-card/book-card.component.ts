import { Component, Input } from '@angular/core';
import { Book } from '../../../core/models/book.model';
import {NgIf} from "@angular/common";

@Component({
    selector: 'app-book-card',
    templateUrl: './book-card.component.html',
    styleUrl: './book-card.component.css',
    imports: [
        NgIf
    ],
    standalone: true
})
export class BookCardComponent {
	@Input() book!: Book;
}
