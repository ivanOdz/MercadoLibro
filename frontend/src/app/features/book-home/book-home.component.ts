import { Component, OnInit, Input, inject, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { BookCardComponent } from '../../shared/components/book-card/book-card.component';
import { BookModelService } from "../../core/services/bookmodel.service";
import { BookService } from "../../core/services/book.service";
import { AuthService } from '../../core/services/auth.service';
import { FilterListComponent } from "../../shared/components/filter-list/filter-list.component";
import { SortComponent } from "../../shared/components/sort/sort.component";
import { BookData2 } from "../../core/models/types";
import {Subscription, combineLatest, filter, switchMap, tap, distinctUntilChanged, forkJoin} from "rxjs";
import {map, take} from "rxjs/operators";
import { InputGroup } from 'primeng/inputgroup';
import { InputGroupAddon } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import {User} from "../../core/models/user.model";
import {BookModel} from "../../core/models/bookModel.model";

@Component({
	selector: 'app-book-home',
	templateUrl: './book-home.component.html',
	styleUrl: './book-home.component.css',
	standalone: true,
    imports: [CommonModule, TranslatePipe, NavbarComponent, RouterModule, FilterListComponent,
        SortComponent, InputGroup, InputGroupAddon, ButtonModule, InputText, FormsModule, BookCardComponent]

})
export class BookHomeComponent implements OnInit {
	
	@ViewChild('searchInput') searchInput!: ElementRef;
	private subscription!: Subscription;

	constructor(private authService: AuthService,
				private route: ActivatedRoute,
				private bookService: BookService,
				private router: Router,
				private bms: BookModelService)
	{ }

	uploadBookModelUrl: string = "/books/add";
	conditionHeaders: Record<string, string> = {};
	genreHeaders: Record<string, string> = {};

	
	showConditionFilter: boolean = true;
	showGenreFilter: boolean = true;
	isSearchActive = false;
	lastSearchQuery: string | null = null;
	
	currentFilters = {
		booksUrl: '',
		state: '',
		genre: '',
		search: '',
	};

	books: BookData2[] = [];
	ngOnInit() {
		this.fetchBooks();
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

	search() {
		if (this.currentFilters.search) {
			this.isSearchActive = true;
			this.router.navigate([], { queryParams: { search: this.currentFilters.search }, queryParamsHandling: 'merge' });
		}
		else {
			this.router.navigate([], { queryParams: { search: null }, queryParamsHandling: 'merge' });
			this.isSearchActive = false;
		}
		
		this.lastSearchQuery = this.currentFilters.search;
		this.searchInput.nativeElement.blur();
	}

	onSearchChange() {
		// Dejo por si la necesito luego
	}

	onBlur() {
		this.currentFilters.search = this.lastSearchQuery || '';
	}
	
	removeFilter(filterKey: keyof typeof this.currentFilters) {
		if (filterKey === 'state') {
			this.currentFilters.state = '';
		}	
		else if (filterKey === 'genre') {
			this.currentFilters.genre = '';
		}
		
		this.router.navigate([], {
			relativeTo: this.route,
			queryParams: { [filterKey]: null },
			queryParamsHandling: 'merge',
		});
		
		this.fetchBooks();
	}
	
	fetchBooks() {
		this.authService.loggedUser$.pipe(
			filter(user => !!user),
			switchMap((user) => {
				this.currentFilters.booksUrl = user.books; // Asignamos la URL de los libros del usuario

				return this.route.queryParams.pipe(
					tap((params) => {
						this.currentFilters.state = params['state'] || '';
						this.currentFilters.genre = params['genre'] || '';
						this.currentFilters.search = params['search'] || '';
						this.showConditionFilter = !params['state'];
						this.showGenreFilter = !params['genre'];
					}),
					switchMap(() => this.bookService.getBooks({ ...this.currentFilters })),
					switchMap(({books, pagination}) => {
						return forkJoin(
							books.map(book =>
								this.bms.getBookModel(book.bookModel).pipe(
									map((bookModel) => ({
										state: book.state,
										available: book.available,
										owner: user,
										bookModel: bookModel,
										images: null,
										self: book.self,
									}) as BookData2)
								)
							)
						);
					})
				);
			})
		).subscribe({
			next: (books) => {
				this.books = books;
			}
		})
	}
}
