import { Component, TemplateRef, ViewChild, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { CardPageComponent } from '../../shared/card-page/card-page.component';
import { PublicationCardComponent } from '../../shared/publication-card/publication.card';
import { PublicationService } from "../../core/services/publication.service";
import { AuthService } from "../../core/services/auth.service";
import { ObservablePublicationData, PublicationData } from "../../core/models/types";
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { User } from "../../core/models/user.model";

@Component({
	selector: 'app-publications',
	templateUrl: `./publications.component.html`,
	styleUrls: ['./publications.component.css'],
	standalone: true,
	imports: [CardPageComponent, PublicationCardComponent, TranslatePipe]

})
export class PublicationsComponent implements OnInit {
	showConditionFilter: boolean = true;
	showGenreFilter: boolean = true;
	publications: PublicationData[] = [];
	private subscription!: Subscription;
	loggedUser: User | null = null;
	
	@ViewChild('publicationCard') publicationCard!: TemplateRef<any>;

	constructor(
		private publicationService: PublicationService,
		private authService: AuthService,
		private route: ActivatedRoute
	) { }

	ngOnInit() {
		this.authService.loggedUser$.subscribe(user => { this.loggedUser = user; });
	}
	
	fetchPublications = (state: string, genre: string, search: string, page: number): ObservablePublicationData => {
		return this.publicationService.getGeneralPublications(state, genre, page, search);
	};
	
	/*
   fetchPublications(url: string | null = null) {
		this.subscription = this.route.queryParams.pipe(
			tap((params) => {
				this.currentFilters.state = params['state'] || '';
				this.currentFilters.genre = params['genre'] || '';
				this.currentFilters.page = params['page'] || 0;
				this.currentFilters.search = params['search'] || '';
			}),
	        switchMap(() => {
				return url
				? this.publicationService.getPublicationsWithDetails(url)
				: this.publicationService.getGeneralPublications(this.currentFilters.state, this.currentFilters.genre, this.currentFilters.page, this.currentFilters.search)
			}),
	        tap((response) => {
				this.publications = response.publicationData || [];
				this.conditionHeaders = response.headers.conditionHeaders
				this.genreHeaders = response.headers.genreHeaders
				this.pagination = response.pagination;
				console.log('PaginationData:', this.pagination);
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
	}*/
}