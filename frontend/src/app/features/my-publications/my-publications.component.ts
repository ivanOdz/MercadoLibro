import { Component, OnDestroy, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription, Observable } from 'rxjs';
import { switchMap, tap, filter, distinctUntilChanged, take, map  } from 'rxjs/operators';
import { HttpHeaders } from '@angular/common/http';
import { AuthService } from '../../core/services/auth.service';
import { PublicationData } from '../../core/models/types';
import { CardPageComponent } from '../../shared/card-page/card-page.component';
import { PublicationService } from "../../core/services/publication.service";
import { Pagination } from "../../core/models/pagination";

@Component({
  selector: 'app-my-publications',
  standalone: true,
  imports: [CardPageComponent],
  templateUrl: './my-publications.component.html',
  styleUrls: ['./my-publications.component.css']
})
export class MyPublicationsComponent {
  showConditionFilter: boolean = true;
  showGenreFilter: boolean = true;
  private subscription!: Subscription;
  publications: any[] = [];  // Asegúrate de declarar un array para las publicaciones

  @ViewChild('publicationCard') publicationCard!: TemplateRef<any>;

  constructor(
      private publicationService: PublicationService,
      private authService: AuthService,
      private route: ActivatedRoute,
  ) {}

  // Método para obtener publicaciones
  fetchMyPublications(filters: any) {
    return this.publicationService.getMyPublications(filters).pipe(
      map(response => ({
        data: response.body?.map(pub => ({
          ...pub,
          book: pub.book?.self ?? '' // Garantiza que book siempre sea un string
        })) || [],
        pagination: new Pagination(response.headers.get('Link')),
        headers: response.headers
      }))
    );
  }

  ngOnInit(): void {
    // Llamar a la función de publicación cuando se cargue el componente
    this.subscription = this.fetchMyPublications({}).subscribe(response => {
      this.publications = response.data;
    });
  }

  // Limpiar la suscripción al destruir el componente
  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
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