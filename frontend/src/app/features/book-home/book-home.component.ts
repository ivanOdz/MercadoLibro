import {Component, OnInit, Input, inject, OnDestroy} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { BookCardComponent } from '../../shared/components/book-card/book-card.component';
import { BookModelService } from "../../core/services/bookmodel.service";
import { BookService } from "../../core/services/book.service";
import { AuthService } from '../../core/services/auth.service';
import { FilterListComponent } from "../../shared/components/filter-list/filter-list.component";
import {Subscription, combineLatest, filter, switchMap, tap, distinctUntilChanged} from "rxjs";
import {take} from "rxjs/operators";
import {BookData2} from "../../core/models/types";

@Component({
	selector: 'app-book-home',
	templateUrl: './book-home.component.html',
	styleUrl: './book-home.component.css',
	standalone: true,
	imports: [ CommonModule, NavbarComponent, BookCardComponent, FilterListComponent ]
})
export class BookHomeComponent implements OnInit, OnDestroy {
	
	private subscription!: Subscription;

	constructor(private authService: AuthService,
				private route: ActivatedRoute,
				private bookModelService: BookModelService,
				private bookService: BookService) {
	}

	conditionHeaders: Record<string, string> = {};
	genreHeaders: Record<string, string> = {};
	books: BookData2[] = [];

	showConditionFilter: boolean = true;
	showGenreFilter: boolean = true;

	currentFilters = {
		state: '',
		genre: '',
		page: 0,
		search: '',
		user: ''
	};

	ngOnInit() {
		this.subscription = this.authService.loggedUser$.pipe(
			filter(user => !!user),
			switchMap((user) => {
				this.currentFilters.user = user.self;

				return this.route.queryParams.pipe(
					tap((params) => {
						this.currentFilters.state = params['state'] || '';
						this.currentFilters.genre = params['genre'] || '';
						this.currentFilters.page = params['page'] || 0;
						this.currentFilters.search = params['search'] || '';

						this.showConditionFilter = !params['state'];
						this.showGenreFilter = !params['genre'];
					}),
					switchMap(() => this.bookService.getMyBooks({ ...this.currentFilters })),
					tap((response) => {
						this.books = response.body || [];
						console.log('Books:', this.books);
						this.processHeaders(response.headers);
					})
				);
			})
		).subscribe({
			error: (err) => {
				console.error('Error:', err);
			}
		});
	}


	ngOnDestroy() {
		this.subscription.unsubscribe();
	}


	private processHeaders(headers: any) {
		const newConditionHeaders: Record<string, string> = {};
		const newGenreHeaders: Record<string, string> = {};
		
		headers.keys().forEach((key: string) => {
			const value = headers.get(key);
			
			if (value !== null) {
				if (key.startsWith("x-bookstate-")) {
					newConditionHeaders[key] = value;
				} else if (key.startsWith("x-genre-")) {
					newGenreHeaders[key] = value;
				}
			}
		});
	
		this.conditionHeaders = { ...newConditionHeaders };
		this.genreHeaders = { ...newGenreHeaders };
	}
	
	/*ngOnInit() {
		this.subscription = combineLatest([this.authService.loggedUser$.pipe(filter(user => !!user)), this.route.queryParams])
		.pipe(
			tap(([user, params]) => {	this.currentFilters.state = params['state'] || '';
										this.currentFilters.genre = params['genre'] || '';
										this.currentFilters.page = params['page'] || 0;
										this.currentFilters.search = params['search'] || ''; })
			, switchMap(() => this.bookService.getBook("http://localhost:8080/books/1"))
		).subscribe(
			{	next: (response) => {	console.log('OK?');
										this.processHeaders(response.headers);
									},
				error: (err) => {	console.error('Error al obtener libros', err); }
			});
	}*/


}
