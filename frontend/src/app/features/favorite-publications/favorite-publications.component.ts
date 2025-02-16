import { Component, TemplateRef, ViewChild } from '@angular/core';
import { filter, Subscription } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { CardPageComponent } from '../../shared/card-page/card-page.component';
import { PublicationCardComponent } from '../../shared/publication-card/publication.card';
import { PublicationService } from "../../core/services/publication.service";
import { AuthService } from "../../core/services/auth.service";
import { ObservablePublicationData, PublicationData } from "../../core/models/types";
import { TranslateService, TranslatePipe } from '@ngx-translate/core';

@Component({
	selector: 'app-favorite-publications',
	templateUrl: './favorite-publications.component.html',
	standalone: true,
	styleUrl: './favorite-publications.component.css',
	imports: [CardPageComponent, PublicationCardComponent, TranslatePipe]
})
export class FavoritePublicationsComponent {
	
	showConditionFilter: boolean = true;
	showGenreFilter: boolean = true;
	publications: PublicationData[] = [];
	private subscription!: Subscription;
  
	@ViewChild('publicationCard') publicationCard!: TemplateRef<any>;

	constructor(
		private publicationService: PublicationService,
		private authService: AuthService,
	) { }
  
	fetchMyFavoritePublications = (state: string, genre: string, search: string, page: number): ObservablePublicationData => {
		return this.authService.loggedUser$.pipe(
			filter(user => !!user),
			switchMap((user) => this.publicationService.getFavoritePublications(user.self, state, genre, page, search))
		);
	};
}

/*

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
              switchMap(() => this.publicationService.getFavoritePublications(user.self, this.currentFilters.state, this.currentFilters.genre, this.currentFilters.page, this.currentFilters.search)),
              tap((response) => {
                // Procesar las publicaciones y los encabezados
                this.publications = response.publicationData
                this.processHeaders(response.headers);
              })
          );
        }),
        switchMap(() =>
            this.authService.loggedUser$.pipe(
                distinctUntilChanged(),
                filter(user => !!user),
                take(1),
                tap((user) => {
                  // Si el usuario está logueado, actualizamos las publicaciones favoritas
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