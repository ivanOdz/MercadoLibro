import { Component, Input, OnInit, TemplateRef, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpHeaders } from "@angular/common/http";
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { FilterListComponent } from "../../shared/filter-list/filter-list.component";
import { InputGroup } from 'primeng/inputgroup';
import { InputGroupAddon } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import { Pagination } from "../../core/models/pagination";
import { PaginatorComponent } from "../../shared/paginator/paginator.component";
import { Observable, tap, switchMap } from "rxjs";
import { SortComponent } from "../../shared/sort/sort.component";

@Component({
	selector: 'card-page',
	templateUrl: './card-page.component.html',
	styleUrl: './card-page.component.css',
	standalone: true,
    imports: [CommonModule, TranslatePipe, NavbarComponent, RouterModule, FilterListComponent,
        InputGroup, InputGroupAddon, ButtonModule, InputText, FormsModule, SortComponent,
		PaginatorComponent, ScrollPanelModule ]

})
export class CardPageComponent implements OnInit {
	
	@Input() pageTitle!: string;
	@Input() items: any[] = []; 
	@Input() fetchMethod!: (params: any) => Observable<{ data: any[], pagination: Pagination, headers: HttpHeaders }>;
	@Input() showSearchBar!: boolean;
	@Input() displaySort!: boolean;
	@Input() cardTemplate!: TemplateRef<any>;
	
	@ViewChild('searchInput') searchInput!: ElementRef;
	
	constructor(private route: ActivatedRoute, private router: Router) { }
	
	conditionHeaders: Record<string, string> = {};
	genreHeaders: Record<string, string> = {};
	isSearchActive = false;
	lastSearchQuery: string | null = null;
	pagination: Pagination | null = null;

	currentFilters = {
			state: '',
			genre: '',
			search: '',
			page: 0,
			available: false,
		};
	
	ngOnInit() {
		this.fetchItems();
	}
	
	fetchItems() {
		this.fetchMethod(this.currentFilters).pipe(
			switchMap((response) => {
				this.pagination = response.pagination;
				this.processHeaders(response.headers);
				this.items = response.data || [];
				return [];
			})
		).subscribe();
	}
	  
	private processHeaders(headers: HttpHeaders) {
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

		this.fetchItems();
	}
}