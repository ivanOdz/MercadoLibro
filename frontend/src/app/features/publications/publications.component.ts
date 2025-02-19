import { Component, TemplateRef, ViewChild, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CardPageComponent } from '../../shared/card-page/card-page.component';
import { PublicationCardComponent } from '../../shared/publication-card/publication.card';
import { PublicationService } from "../../core/services/publication.service";
import { AuthService } from "../../core/services/auth.service";
import { ObservablePublicationData, PublicationData } from "../../core/models/types";
import { TranslatePipe } from '@ngx-translate/core';
import { User } from "../../core/models/user.model";
import {ProgressSpinner} from "primeng/progressspinner";
import {Button} from "primeng/button";

@Component({
	selector: 'app-publications',
	templateUrl: `./publications.component.html`,
	styleUrls: ['./publications.component.css'],
	standalone: true,
	imports: [CardPageComponent, PublicationCardComponent, TranslatePipe, ProgressSpinner, Button]

})
export class PublicationsComponent implements OnInit {
	
	showConditionFilter: boolean = true;
	showGenreFilter: boolean = true;
	publications: PublicationData[] = [];
	loggedUser: User | null = null;
	
	@ViewChild('publicationCard') publicationCard!: TemplateRef<any>;

	constructor(
		private publicationService: PublicationService,
		private authService: AuthService,
		private router: Router
	) { }

	ngOnInit() {
		this.authService.loggedUser$.subscribe(user => { this.loggedUser = user; });
	}
	
	fetchPublications = (state: string, genre: string, search: string, page: number, sort: string): ObservablePublicationData => {
		return this.publicationService.getGeneralPublications(state, genre, page, search, sort);
	};

	redirectToBooks() {
		this.router.navigate(['/books'])
	}
}