import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PublicationService } from '../../core/services/publication.service';
import { AuthService } from '../../core/services/auth.service';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { FilterListComponent } from "../../shared/components/filter-list/filter-list.component";
import { SortComponent } from "../../shared/components/sort/sort.component";

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
      private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.currentFilters.state = params['state'] || '';
      this.currentFilters.genre = params['genre'] || '';
      this.currentFilters.page = params['page'] || 0;
      this.currentFilters.search = params['search'] || '';

      this.fetchPublications();
    });
  }

  fetchPublications() {
    this.publicationService.getPublications({
      state: this.currentFilters.state,
      genre: this.currentFilters.genre,
      page: this.currentFilters.page,
      search: this.currentFilters.search
    }).subscribe({
      next: (response) => {
        console.log('Publicaciones actualizadas:', response.body);

        const newConditionHeaders: Record<string, string> = {};
        const newGenreHeaders: Record<string, string> = {};

        response.headers.keys().forEach((key: string) => {
          const value = response.headers.get(key);
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

        },
      error: (err) => {
        console.error('Error al obtener las publicaciones:', err);
      }
    });
  }
}