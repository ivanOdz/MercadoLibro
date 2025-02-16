import { Component, Input, OnInit } from "@angular/core";
import { CommonModule } from '@angular/common';
import { PublicationData } from "../../core/models/types";
import { Router } from "@angular/router";
import { TranslatePipe } from '@ngx-translate/core';
import { environment } from "../../../environments/environment";
import { User } from "../../core/models/user.model";
import { PublicationService } from "../../core/services/publication.service";

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
	
    constructor(private router: Router, private publicationService: PublicationService) { }

	ngOnInit() {
		this.bookImage = this.getBookImage();
	}

    goToPublicationDetail() {
        if (this.publication.self) {
            const path = this.publication.self.replace(/^.*\/api/, '');
            this.router.navigate([path],{ queryParams: { origen: 'publications' } });
        }
    }
	
	getBaseUrl() {
		return `${environment.production? environment.productionUrl  : environment.developmentUrl}`;
	}
	
	getBookImage(): string {
		return	this.publication.book?.images?.length ? this.getBaseUrl() + this.publication.book.images[0] :
				this.publication.book?.bookModel?.cover ? this.getBaseUrl() + this.publication.book.bookModel.cover :
				this.defaultImage;
	}
	
	toggleLike(event: Event): void {
		
		event.stopPropagation();
		
		if (!this.loggedUser) {
			console.warn('User not logged!');
			return;
		}

		if (this.publication.favoritePublication) {
			this.publicationService.unlikePublication(this.publication).subscribe({
				next: () => console.log('Publicación eliminada de favoritos'),
				error: (err) => console.error('Error al eliminar de favoritos', err)
			});
		} else {
			this.publicationService.likePublication(this.publication, this.loggedUser).subscribe({
				next: () => console.log('Publicación agregada a favoritos'),
				error: (err) => console.error('Error al agregar a favoritos', err)
			});
		}
	}
}