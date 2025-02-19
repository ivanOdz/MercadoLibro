import {Component, Input, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { TranslatePipe } from '@ngx-translate/core';

import { BookData } from '../../core/models/types';
import { NgIf } from "@angular/common";
import {Dialog} from "primeng/dialog";
import {Divider} from "primeng/divider";
import {switchMap, take} from "rxjs/operators";
import {AuthService} from "../../core/services/auth.service";
import {UserService} from "../../core/services/user.service";
import {filter} from "rxjs";
import {Location} from "../../core/models/location.model";
import {FormsModule} from "@angular/forms";
import {Button} from "primeng/button";
import {PublicationService} from "../../core/services/publication.service";
import {Router} from "@angular/router";
import {User} from "../../core/models/user.model";
import {Select} from "primeng/select";
import {environment} from "../../../environments/environment";
import {MatIcon} from "@angular/material/icon";
import {BookService} from "../../core/services/book.service";

@Component({
    selector: 'app-book-card',
    templateUrl: './book-card.component.html',
    styleUrl: './book-card.component.css',
    standalone: true,
	imports: [CommonModule, NgIf, TranslatePipe, Dialog, Divider, FormsModule, Button, Select, MatIcon]
})
export class BookCardComponent implements OnInit{
	
	@Input() book!: BookData;
	@Input() showOwner: boolean = false;
	@Input() showGenre: boolean = true;
	@Input() showState: boolean = true;
	
	ownerName: string = '...';
	bookImage!: string;
	defaultImage: string = './assets/book.jpg';
	errorNoLocation: boolean = false;

	constructor(private http: HttpClient, private as: AuthService, private us: UserService,
				private ps: PublicationService, private bs: BookService,
				private router: Router) { }

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
	
	getBookImage(): string {
		return	this.book.images?.length ? this.book.images[0] :
				this.book.bookModel?.cover ? this.getBaseUrl() + this.book.bookModel.cover :
				this.defaultImage;
	}

	getBaseUrl() {
		return `${environment.production? environment.productionUrl  : environment.developmentUrl}`;
	}

	modalPublicationVisible: boolean = false;
	userLocations: Location[] = [];
	selectedLocation: Location | null = null
	modalSeePublicationVisible: boolean = false;
	isStateModalOpen = false;
	condition: string = '';
	newCondition: string = '';
	bookStates = ['new', 'like_new', 'very_good', 'good', 'acceptable', 'worn'];

	openStateModal() {
		this.condition = this.book.state;
		this.isStateModalOpen = true;
	}

	closeStateModal() {
		this.newCondition = '';
		this.isStateModalOpen = false;
	}

	isValidState() {
		return this.newCondition.length > 0;
	}

	updateState() {
		this.bs.updateBookstate(this.book, this.newCondition).subscribe({});
		this.closeStateModal();
	}

	openModal() {
		this.modalPublicationVisible = true;

		this.as.loggedUser$.pipe(
			take(1),
			filter((user) => !!user),
			switchMap((user) => {
				return this.us.getLocations(user);
			})
		).subscribe((locations) => {
			this.userLocations = locations;
		});
	}


	closeModal() {
		this.modalPublicationVisible = false;
	}

	createPublication() {
		if(!this.selectedLocation) {
			this.errorNoLocation = true;
			return;
		}

		this.modalPublicationVisible = false;

		this.as.loggedUser$.pipe(
			filter((user) => !!user),
			switchMap((user: User) =>{
				return this.ps.createPublication(user.self, this.book.self, this.selectedLocation)
			}
			)
		).subscribe(() => {
			this.router.navigate(['publications/mine'])
		});


	}

	goToMyPublications() {
		this.router.navigate(['publications/mine']);
	}

	getStateClass() {
		return {
			'book-state-new': this.book.state === 'NEW',
			'book-state-like-new': this.book.state === 'LIKE_NEW',
			'book-state-very-good': this.book.state === 'VERY_GOOD',
			'book-state-good': this.book.state === 'GOOD',
			'book-state-aceptable': this.book.state === 'ACCEPTABLE',
			'book-state-worn': this.book.state === 'WORN'
		};
	}

    redirectToProfile() {
        this.router.navigate(['/auth/profile'])
    }
}
