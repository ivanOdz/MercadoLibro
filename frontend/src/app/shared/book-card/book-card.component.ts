import {Component, Input, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { TranslatePipe } from '@ngx-translate/core';

import { BookData } from '../../core/models/types';
import { NgIf } from "@angular/common";

@Component({
    selector: 'app-book-card',
    templateUrl: './book-card.component.html',
    styleUrl: './book-card.component.css',
    standalone: true,
	imports: [ CommonModule, NgIf, TranslatePipe ]
})
export class BookCardComponent implements OnInit{
	
	@Input() book!: BookData;
	@Input() showOwner: boolean = false;
	@Input() showGenre: boolean = true;
	@Input() showState: boolean = true;
	
	ownerName: string = '...';
	bookImage!: string;
	defaultImage: string = './assets/book.jpg';
	
	constructor(private http: HttpClient) { }

	ngOnInit() {
		console.log(this.book);
		this.bookImage = this.getBookImage();
		
		if (this.book.owner && this.book.owner.username) {
			this.ownerName = this.book.owner.username;
		} else {
			this.ownerName = 'BOOK_CARD.UNKNOWN';
		}
	}
		
	private fetchOwnerDetails(ownerUrl: string) {
		this.http.get<any>(ownerUrl).subscribe({
			next: (data) => {
				this.ownerName = data.username || 'BOOK_CARD.UNKNOWN';
			},
			error: () => {
				this.ownerName = 'BOOK_CARD.NOT_FOUND';
			}
		});
	}
	
	private getBookImage(): string {
		return	this.book.images?.length ? this.book.images[0] :
				this.book.bookModel?.coverUri ? this.book.bookModel.coverUri :
				this.defaultImage;
	}
	
}
