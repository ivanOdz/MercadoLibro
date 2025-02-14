import {Component, OnDestroy, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { PublicationService } from '../../core/services/publication.service';
import { AuthService } from '../../core/services/auth.service';
import { TranslatePipe } from '@ngx-translate/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { FilterListComponent } from "../../shared/filter-list/filter-list.component";
import { SortComponent } from "../../shared/sort/sort.component";
import { distinctUntilChanged, filter, Subscription, switchMap, tap } from "rxjs";
import { PublicationData } from "../../core/models/types";
import { take } from "rxjs/operators";
import { NgForOf, NgIf } from "@angular/common";
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { PublicationCardComponent } from "../../shared/publication-card/publication.card";
import { Pagination } from "../../core/models/pagination";
import { PaginatorComponent } from "../../shared/paginator/paginator.component";
import { InputText } from 'primeng/inputtext';
import { InputGroup } from 'primeng/inputgroup';
import { InputGroupAddon } from 'primeng/inputgroupaddon';

@Component({
	selector: 'app-publications',
	templateUrl: `./publications.component.html`,
	styleUrls: ['./publications.component.css'],
	standalone: true,
	imports: [ NavbarComponent, TranslatePipe, RouterModule, FilterListComponent, NgIf,
		PublicationCardComponent, NgForOf, ScrollPanelModule,SortComponent ]

})
export class PublicationsComponent implements OnInit, OnDestroy {

	@ViewChild('searchInput') searchInput!: ElementRef;
	
	constructor(
		private publicationService: PublicationService,
		private authService: AuthService,
		private route: ActivatedRoute,
		private router: Router,
	) {}
	
	conditionHeaders: Record<string, string> = {};
	genreHeaders: Record<string, string> = {};
	isSearchActive = false;
	lastSearchQuery: string | null = null;
	
	private subscription!: Subscription;
	publications: PublicationData[] = [];
	pagination: Pagination | null= null;

	currentFilters = {
		state: '',
		genre: '',
		page: 0,
		search: ''
	};

	ngOnInit() {
		this.fetchPublications();
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
  	
		this.fetchPublications();
  	}
  
   fetchPublications(url: string | null = null) {
		this.subscription = this.route.queryParams.pipe(
			tap((params) => {
				this.currentFilters.state = params['state'] || '';
				this.currentFilters.genre = params['genre'] || '';
				this.currentFilters.page = params['page'] || 0;
				this.currentFilters.search = params['search'] || '';
			}),
	        switchMap(() => this.publicationService.getGeneralPublications(this.currentFilters)),
	        tap((response) => {
				this.publications = response.body || [];
				this.processHeaders(response.headers);
			}),
			switchMap(() =>
				this.authService.loggedUser$.pipe(
					distinctUntilChanged(),
					filter(user => !!user),
					take(1),
					tap((user) => {
						this.publicationService.setFavoritePublication(user!.self, this.publications).subscribe();
					})
				)
			)
		).subscribe({
			error: (err) => {
				console.error('Error:', err);
			}
		});
	}
}