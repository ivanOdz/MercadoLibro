import { Component, Input, OnInit, TemplateRef, ViewChild, ElementRef, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TranslatePipe } from '@ngx-translate/core';
import { NavbarComponent } from "../navbar/navbar.component";
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { FilterListComponent } from "../filter-list/filter-list.component";
import { InputGroup } from 'primeng/inputgroup';
import { InputGroupAddon } from 'primeng/inputgroupaddon';
import { ButtonModule } from 'primeng/button';
import { InputText } from 'primeng/inputtext';
import { Pagination } from "../../core/models/pagination";
import { PaginatorComponent } from "../paginator/paginator.component";
import { switchMap } from "rxjs";
import { SortComponent } from "../sort/sort.component";
import {ObservablePublicationData} from "../../core/models/types";

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
	@Input() fetchMethod!: (state: string, genre: string, search: string, page: number) => ObservablePublicationData;
	@Input() showSearchBar!: boolean;
	@Input() displaySort!: boolean;
	@Input() displayGridStyle: boolean = false;
	
	@Input() items: any[] = []; 
	@Input() cardTemplate!: TemplateRef<any>;
	
	@ViewChild('searchInput') searchInput!: ElementRef;
	
	constructor(private route: ActivatedRoute, private router: Router) { }
	
	conditionHeaders: Record<string, string> = {};
	genreHeaders: Record<string, string> = {};
	isSearchActive = false;
	lastSearchQuery: string | null = null;
	pagination: Pagination | null = null;
	stateFilterApplied: boolean = false;
	genreFilterApplied: boolean = false;
	totalResults: number = 0;
	
	currentFilters = {
			state: '',
			genre: '',
			search: '',
			page: 0
		};
	
	ngOnInit() {
		this.fetchItems(undefined, 0);
	}
	
	fetchItems(url: string | undefined, page: number) {
		this.currentFilters.page = page;
		this.fetchMethod(this.currentFilters.state, this.currentFilters.genre, this.currentFilters.search, this.currentFilters.page).pipe(
			switchMap((response) => {
				this.pagination = response.pagination;
				this.conditionHeaders = response.headers.conditionHeaders;
				this.genreHeaders = response.headers.genreHeaders;
				this.items = response.publicationData;
				this.totalResults = response.totalResults;
				console.log("Total Results:", this.totalResults);
				return [];
			})
		).subscribe({
			next: (items) => {
				this.items = items;
			}
		});
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
			this.stateFilterApplied = false;
		}	
		else if (filterKey === 'genre') {
			this.currentFilters.genre = '';
			this.genreFilterApplied = false;
		}

		this.router.navigate([], {
			relativeTo: this.route,
			queryParams: { [filterKey]: null },
			queryParamsHandling: 'merge',
		});

		this.fetchItems(undefined, 0);
	}
	
	processHeaders(headersData: { conditionHeaders: Record<string, string>, genreHeaders: Record<string, string> }) {
		if (!headersData) return;

		this.conditionHeaders = { ...headersData.conditionHeaders };
		this.genreHeaders = { ...headersData.genreHeaders };
	}
	
	onFilterUpdate(filter: { param: string, value: string }) {

		if (filter.param === "state") {
			this.currentFilters.state = filter.value;
			this.stateFilterApplied = true;
		}
		else if (filter.param === "genre") {
			this.currentFilters.genre = filter.value;
			this.genreFilterApplied = true;
		}
				
		this.fetchItems(undefined, 0);
	}
}