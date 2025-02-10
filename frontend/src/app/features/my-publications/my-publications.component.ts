import {Component, OnDestroy, OnInit} from '@angular/core';
import {PublicationService} from "../../core/services/publication.service";
import {AuthService} from "../../core/services/auth.service";
import {ActivatedRoute} from "@angular/router";
import {distinctUntilChanged, filter, Subscription, switchMap, tap} from "rxjs";
import {PublicationData2} from "../../core/models/types";
import {take} from "rxjs/operators";
import {FilterListComponent} from "../../shared/components/filter-list/filter-list.component";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {SortComponent} from "../../shared/components/sort/sort.component";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-my-publications',
    imports: [
        FilterListComponent,
        NavbarComponent,
        SortComponent,
        NgIf
    ],
  templateUrl: './my-publications.component.html',
  standalone: true,
  styleUrl: './my-publications.component.css'
})
export class MyPublicationsComponent implements OnInit, OnDestroy {

  conditionHeaders: Record<string, string> = {};
  genreHeaders: Record<string, string> = {};

    showConditionFilter: boolean = true;
    showGenreFilter: boolean = true;

    private subscription!: Subscription;

  publications: PublicationData2[] = [];

  currentFilters = {
    state: '',
    genre: '',
    page: 0,
    search: '',
    user: ''
  };

  constructor(
      private publicationService: PublicationService,
      private authService: AuthService,
      private route: ActivatedRoute,
  ) {}

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
}
