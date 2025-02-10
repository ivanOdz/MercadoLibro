import { Component, OnInit, Input, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { Book } from './../../core/models/book.model';
import { BookCardComponent } from './../../shared/components/book-card/book-card.component';
import { BookModelService } from "../../core/services/bookmodel.service";
import { BookService } from "../../core/services/book.service";
import { AuthService } from '../../core/services/auth.service';
import { FilterListComponent } from "../../shared/components/filter-list/filter-list.component";
import { SortComponent } from "../../shared/components/sort/sort.component";
import { Subscription, combineLatest, filter, switchMap, tap } from "rxjs";

@Component({
	selector: 'app-book-home',
	templateUrl: './book-home.component.html',
	styleUrl: './book-home.component.css',
	standalone: true,
	imports: [ CommonModule, NavbarComponent, BookCardComponent, FilterListComponent, SortComponent ]
})
export class BookHomeComponent implements OnInit {
	
	private subscription!: Subscription;
	private authService: AuthService = inject(AuthService);
	private route: ActivatedRoute = inject(ActivatedRoute);
	private bookModelService: BookModelService = inject(BookModelService);
	private bookService: BookService = inject(BookService);
	
	conditionHeaders: Record<string, string> = {};
	genreHeaders: Record<string, string> = {};
	books: Book[] = [];
	
	currentFilters = {
		state: '',
		genre: '',
		page: 0,
		search: ''
	};
	
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
	
	ngOnInit() {
		this.subscription = combineLatest([this.authService.loggedUser$.pipe(filter(user => !!user)), this.route.queryParams])
		.pipe(
			tap(([user, params]) => {	this.currentFilters.state = params['state'] || '';
										this.currentFilters.genre = params['genre'] || '';
										this.currentFilters.page = params['page'] || 0;
										this.currentFilters.search = params['search'] || ''; })
			, switchMap(() => this.bookService.getBook("http://localhost:8080/books/1"))
		).subscribe(
			{	next: (response) => {	console.log('OK?');
										/*this.processHeaders(response.headers);*/
									},
				error: (err) => {	console.error('Error al obtener libros', err); }
			});
	}
	  
	ngOnDestroy() {
		this.subscription.unsubscribe();
	}
}
