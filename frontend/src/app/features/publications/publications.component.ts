import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PublicationService } from '../../core/services/publication.service';
import { AuthService } from '../../core/services/auth.service';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { FilterListComponent } from "../../shared/components/filter-list/filter-list.component";
import { SortComponent } from "../../shared/components/sort/sort.component";
import {combineLatest, distinctUntilChanged, filter, of, startWith, Subscription, switchMap, tap} from "rxjs";
import {PublicationData2} from "../../core/models/types";
import {map, take} from "rxjs/operators";

@Component({
  selector: 'app-publications',
  templateUrl: `./publications.component.html`,
  standalone: true,
  imports: [
    NavbarComponent,
    FilterListComponent,
    SortComponent
  ],
  styleUrls: ['./publications.component.css']
})
export class PublicationsComponent implements OnInit {

  conditionHeaders: Record<string, string> = {};
  genreHeaders: Record<string, string> = {};

  private subscription!: Subscription;

  publications: PublicationData2[] = [];

  currentFilters = {
    state: '',
    genre: '',
    page: 0,
    search: ''
  };

  constructor(
      private publicationService: PublicationService,
      private authService: AuthService,
      private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.subscription = this.route.queryParams.pipe(
        tap((params) => {
          this.currentFilters.state = params['state'] || '';
          this.currentFilters.genre = params['genre'] || '';
          this.currentFilters.page = params['page'] || 0;
          this.currentFilters.search = params['search'] || '';
        }),
        switchMap(() => this.publicationService.getPublicationsWithDetails(this.currentFilters)), // Obtener publicaciones
        tap((response) => {
          this.publications = response.body || []; // Guardamos las publicaciones obtenidas
          this.processHeaders(response.headers);
        }),
        switchMap((response) =>
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