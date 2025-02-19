import { Component, TemplateRef, ViewChild, OnInit } from '@angular/core';
import { CardPageComponent } from '../../shared/card-page/card-page.component';
import { PublicationCardComponent } from '../../shared/publication-card/publication.card';
import { PublicationService } from "../../core/services/publication.service";
import { AuthService } from "../../core/services/auth.service";
import { ObservablePublicationData, PublicationData } from "../../core/models/types";
import { TranslatePipe } from '@ngx-translate/core';
import { User } from "../../core/models/user.model";
import {Button} from "primeng/button";
import {ProgressSpinner} from "primeng/progressspinner";
import {Router} from "@angular/router";

@Component({
	selector: 'app-favorite-publications',
	templateUrl: './favorite-publications.component.html',
	styleUrl: './favorite-publications.component.css',
	standalone: true,
    imports: [CardPageComponent, PublicationCardComponent, TranslatePipe, Button, ProgressSpinner]
})
export class FavoritePublicationsComponent implements OnInit {
	
	publications: PublicationData[] = [];
	loggedUser: User | null = null;

	@ViewChild('publicationCard') publicationCard!: TemplateRef<any>;

	constructor(
		private publicationService: PublicationService,
		private authService: AuthService,
		private router: Router,
	) { }
  

	ngOnInit() {
		this.authService.loggedUser$.subscribe(user => { this.loggedUser = user; });
	}
	
	fetchMyFavoritePublications = (state: string, genre: string, search: string, page: number, sort: string): ObservablePublicationData => {
		return this.publicationService.getFavoritePublications(this.loggedUser!.favorites, state, genre, page, search, sort);
	};

	redirectToPublications() {
		this.router.navigate(['/publications'])
	}
}
