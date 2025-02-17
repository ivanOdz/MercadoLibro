import { Component, Input, OnInit } from "@angular/core";
import { CommonModule } from '@angular/common';
import { PublicationData } from "../../core/models/types";
import { Router } from "@angular/router";
import { TranslatePipe } from '@ngx-translate/core';
import { environment } from "../../../environments/environment";
import { User } from "../../core/models/user.model";
import { PublicationService } from "../../core/services/publication.service";
import {catchError, EMPTY, throwError} from "rxjs";
import {AuthService} from "../../core/services/auth.service";

@Component({
    selector: 'publication-card',
    templateUrl: './publication.card.html',
    styleUrl: './publication.card.css',
    standalone: true,
	imports: [CommonModule, TranslatePipe]
})
export class PublicationCardComponent implements OnInit {
    @Input() publication!: PublicationData;
	@Input() loggedUser!: User | null;
	@Input() showLikeHeart: boolean = false;
	
	bookImage!: string;
	defaultImage: string = './assets/book.jpg';
	
    constructor(private router: Router, private publicationService: PublicationService, private au: AuthService) { }

	ngOnInit() {
		this.bookImage = this.getBookImage();
		this.getIfItIsFavorite();
	}

    goToPublicationDetail() {
        if (this.publication.self) {
            const path = this.publication.self.replace(/^.*\/api/, '');
            this.router.navigate([path],{ queryParams: { origen: 'publications' } });
        }
    }

	getIfItIsFavorite() {
		if (this.showLikeHeart) {
			// Verificamos si el usuario está loggeado antes de hacer cualquier acción con favoritos
			this.au.loggedUser$.pipe(
				catchError((err) => {
					if (err.status === 401) {
						// El usuario no está loggeado, no hacemos nada más
						return EMPTY;
					}
					return throwError(() => err); // Propagamos otros errores
				})
			).subscribe({
				next: (user) => {
					if (user) {
						// Si el usuario está loggeado, lo asignamos y buscamos los favoritos
						this.loggedUser = user;

						// Llamamos a getFavoritePublication solo si el usuario está loggeado
						this.publicationService.getFavoritePublication(this.publication.isFavoriteTemplate, this.loggedUser!.self)
							.pipe(
								catchError((err) => {
									if (err.status === 401) {
										// Si no está loggeado
										return EMPTY;
									} else if (err.status === 404) {
										// Si no se encuentra como favorito
										return EMPTY;
									}
									return throwError(() => err); // Propagamos otros errores
								})
							)
							.subscribe({
								next: (favoritePublication) => {
									// Si hay favoritos, los asignamos
									this.publication.favoritePublication = favoritePublication;
								},
								error: (err) => {
									console.error('Error inesperado:', err);
								}
							});
					}
				},
				error: (err) => {
					console.error('Error inesperado:', err);
				}
			});
		}
	}



	getBaseUrl() {
		return `${environment.production? environment.productionUrl  : environment.developmentUrl}`;
	}
	
	getBookImage(): string {
		return	this.publication.book?.images?.length ? this.publication.book.images[0] : (this.publication.book?.bookModel?.cover ? this.getBaseUrl() + this.publication.book.bookModel.cover : this.defaultImage);
	}
	
	toggleLike(event: Event): void {
		
		event.stopPropagation();
		
		if (!this.loggedUser) {
			console.warn('User not logged!');
			return;
		}

		if (this.publication.favoritePublication) {
			this.publicationService.unlikePublication(this.publication).subscribe({
				next: () => this.publication.favoritePublication = null,
//				error: (err) => console.error('Error eliminating', err)
			});
		} else {
			this.publicationService.likePublication(this.publication, this.loggedUser).subscribe({
				next: () => this.getIfItIsFavorite(),
//				error: (err) => console.error('Error posting', err)
			});
		}
	}
	
	getFormattedLocations(publication: PublicationData): string {
		
		if (!publication.locations || publication.locations.length === 0) {
			return '?';
		}

		const locationNames = publication.locations.map(loc => loc.location);
		if (locationNames.length > 3) {
			return `${locationNames.slice(0, 3).join(', ')}...`;
	    }

		return locationNames.join(', ');
	}

}