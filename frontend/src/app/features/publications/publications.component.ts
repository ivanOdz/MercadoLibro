import { Component, TemplateRef, ViewChild, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { filter, Subscription } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';
import { CardPageComponent } from '../../shared/card-page/card-page.component';
import { PublicationCardComponent } from '../../shared/publication-card/publication.card';
import { PublicationService } from "../../core/services/publication.service";
import { AuthService } from "../../core/services/auth.service";
import { ObservablePublicationData, PublicationData } from "../../core/models/types";
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { User } from "../../core/models/user.model";

@Component({
	selector: 'app-publications',
	templateUrl: `./publications.component.html`,
	styleUrls: ['./publications.component.css'],
	standalone: true,
	imports: [CardPageComponent, PublicationCardComponent, TranslatePipe]

})
export class PublicationsComponent implements OnInit {
	
	showConditionFilter: boolean = true;
	showGenreFilter: boolean = true;
	publications: PublicationData[] = [];
	loggedUser: User | null = null;
	private subscription!: Subscription;
	
	@ViewChild('publicationCard') publicationCard!: TemplateRef<any>;

	constructor(
		private publicationService: PublicationService,
		private authService: AuthService,
		private route: ActivatedRoute
	) { }

	ngOnInit() {
		this.authService.loggedUser$.subscribe(user => { this.loggedUser = user; });
	}
	
	fetchPublications = (state: string, genre: string, search: string, page: number): ObservablePublicationData => {
		return this.publicationService.getGeneralPublications(state, genre, page, search);
	};
}