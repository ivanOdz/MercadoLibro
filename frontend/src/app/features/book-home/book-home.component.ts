import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { BookCardComponent } from '../../shared/book-card/book-card.component';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { BookModelService } from "../../core/services/bookmodel.service";
import { BookService } from "../../core/services/book.service";
import { AuthService } from '../../core/services/auth.service';
import { FilterListComponent } from "../../shared/filter-list/filter-list.component";
import { BookData } from "../../core/models/types";
import { filter, switchMap, tap, forkJoin } from "rxjs";
import { map } from "rxjs/operators";
import { InputGroup } from 'primeng/inputgroup';
import { InputGroupAddon } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import { Pagination } from "../../core/models/pagination";
import { PaginatorComponent } from "../../shared/paginator/paginator.component";
import {SortComponent} from "../../shared/sort/sort.component";
import {ProgressSpinner} from "primeng/progressspinner";

@Component({
	selector: 'app-book-home',
	templateUrl: './book-home.component.html',
	styleUrl: './book-home.component.css',
	standalone: true,
	imports: [CommonModule, TranslatePipe, NavbarComponent, RouterModule, FilterListComponent,
		InputGroup, InputGroupAddon, ButtonModule, InputText, FormsModule,
		BookCardComponent, PaginatorComponent, ScrollPanelModule, SortComponent, ProgressSpinner]

})
export class BookHomeComponent implements OnInit {

	@ViewChild('searchInput') searchInput!: ElementRef;

	constructor(private authService: AuthService,
				private route: ActivatedRoute,
				private bookService: BookService,
				private router: Router,
				private bms: BookModelService)
	{ }

	uploadBookModelUrl: string = "/books/library";
	conditionHeaders: Record<string, string> = {};
	genreHeaders: Record<string, string> = {};
	isSearchActive = false;
	lastSearchQuery: string | null = null;
	
	currentFilters = {
		booksUrl: '',
		state: '',
		genre: '',
		search: '',
		available: false,
		sort: '',
	};

	books: BookData[] = [];
	pagination: Pagination | null= null;
	resetPaginatorNumber: boolean = false;
	firstLoad: boolean = true;
	loading: boolean = true;
	
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

	onSortUpdate(sort: string) {
		this.currentFilters.sort = sort;
		this.resetPaginatorNumber = true;
		setTimeout(() => (this.resetPaginatorNumber = false), 300);
		this.fetchBooks();
	}

	fetchBooks(url: string | null = null) {
		this.loading = true;
		this.authService.loggedUser$.pipe(
			filter(user => !!user),
			switchMap((user) => {
				this.currentFilters.booksUrl = user.books;

				return this.route.queryParams.pipe(
					tap((params) => {
						this.currentFilters.state = params['state'] || '';
						this.currentFilters.genre = params['genre'] || '';
						this.currentFilters.search = params['search'] || '';
						this.currentFilters.sort = params['sort'] || '';
					}),
					switchMap(() => {
						return url
							? this.bookService.getBooksByUrl(url)
							: this.bookService.getBooks({ ...this.currentFilters });
					}),
					tap((response) => {
					    this.processHeaders(response.headers);
						this.pagination = response.pagination;
					}),
					switchMap((response) => {
						if(response.books.length === 0) {
							this.loading = false;
							this.books = [];
						}
						return forkJoin(
							response.books.map(book =>
								this.bms.getBookModel(book.bookModel).pipe(
									map((bookModel) => ({
										state: book.state,
										available: book.available,
										owner: user,
										bookModel: bookModel,
										images: book.images,
										self: book.self,
									}) as BookData)
								)
							)
						);
					})
				);
			})
		).subscribe({
			next: (books) => {
				this.books = books;
				this.loading = false;
				this.firstLoad ? (this.firstLoad = false) : null;
			},
			complete: () => {
				this.loading = false;
				this.firstLoad ? (this.firstLoad = false) : null;
			}
		})
	}
}
