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

  conditionHeaders = {
    "X-bookstate-new": "bookstate.new=5",
    "X-bookstate-like-new": "bookstate.like.new=2",
    "X-bookstate-very-good": "bookstate.very.good=8",
    "X-bookstate-good": "bookstate.good=1",
    "X-bookstate-acceptable": "bookstate.acceptable=6",
    "X-bookstate-worn": "bookstate.worn=4"
  };

  genreHeaders = {
    "X-genre-fiction": "genre.fiction=12",
    "X-genre-non-fiction": "genre.non.fiction=8",
    "X-genre-mystery": "genre.mystery=4"
  };

  // Variables para los filtros y la página
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
    // Usamos el servicio para obtener las publicaciones con los filtros actuales
    this.publicationService.getPublications({
      state: this.currentFilters.state,
      genre: this.currentFilters.genre,
      page: this.currentFilters.page,
      search: this.currentFilters.search
    }).subscribe({
      next: (response) => {
        console.log('Publicaciones actualizadas:', response);
        // Aquí podrías hacer algo con la respuesta, como actualizar la vista
      },
      error: (err) => {
        console.error('Error al obtener las publicaciones:', err);
      }
    });
  }

}
