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
	selector: 'app-my-publications',
	templateUrl: './my-publications.component.html',
	styleUrls: ['./my-publications.component.css'],
	standalone: true,
	imports: [CardPageComponent, PublicationCardComponent, TranslatePipe]
})
export class MyPublicationsComponent {
	showConditionFilter: boolean = true;
	showGenreFilter: boolean = true;
	publications: PublicationData[] = [];
	private subscription!: Subscription;
  
	@ViewChild('publicationCard') publicationCard!: TemplateRef<any>;

	constructor(
		private publicationService: PublicationService,
		private authService: AuthService,
	) { }
  
	fetchMyPublications = (state: string, genre: string, search: string, page: number): ObservablePublicationData => {
		return this.authService.loggedUser$.pipe(
			filter(user => !!user),
			switchMap((user) => this.publicationService.getMyPublications(user.publications, state, genre, page, search))
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
            switchMap(() => this.publicationService.getMyPublications({ ...this.currentFilters })),
            tap((response) => {
              // Procesar las publicaciones y los encabezados
              this.publications = response.body || [];
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