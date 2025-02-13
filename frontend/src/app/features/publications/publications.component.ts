import {Component, OnDestroy, OnInit} from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { PublicationService } from '../../core/services/publication.service';
import { AuthService } from '../../core/services/auth.service';
import { TranslatePipe } from '@ngx-translate/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { FilterListComponent } from "../../shared/filter-list/filter-list.component";
import { SortComponent } from "../../shared/sort/sort.component";
import { distinctUntilChanged, filter, Subscription, switchMap, tap } from "rxjs";
import { PublicationData } from "../../core/models/types";
import { take } from "rxjs/operators";
import { NgIf } from "@angular/common";

@Component({
  selector: 'app-publications',
  templateUrl: `./publications.component.html`,
  standalone: true,
  imports: [
    NavbarComponent,
	TranslatePipe,
	RouterModule,
    FilterListComponent,
    SortComponent,
    NgIf
  ],
  styleUrls: ['./publications.component.css']
})
export class PublicationsComponent implements OnInit, OnDestroy {

  conditionHeaders: Record<string, string> = {};
  genreHeaders: Record<string, string> = {};

  private subscription!: Subscription;

  publications: PublicationData[] = [];

  currentFilters = {
    state: '',
    genre: '',
    page: 0,
    search: ''
  };

  showConditionFilter: boolean = true;
  showGenreFilter: boolean = true;

  constructor(
      private publicationService: PublicationService,
      private authService: AuthService,
      private route: ActivatedRoute,
	  private router: Router,
  ) {}

  removeFilter(filterKey: keyof typeof this.currentFilters) {
  	if (filterKey === 'state') {
  		this.currentFilters.state = '';
  	}	
  	else if (filterKey === 'genre') {
  		this.currentFilters.genre = '';
  	}
  	
  	this.router.navigate([], {
  		relativeTo: this.route,
  		queryParams: { [filterKey]: null },
  		queryParamsHandling: 'merge',
  	});
  	
//  	this.fetchPublications();
//	this.ngOnInit();
  }
  
  ngOnInit() {
    this.subscription = this.route.queryParams.pipe(
        tap((params) => {
          this.currentFilters.state = params['state'] || '';
          this.currentFilters.genre = params['genre'] || '';
          this.currentFilters.page = params['page'] || 0;
          this.currentFilters.search = params['search'] || '';

          this.showConditionFilter = !params['state'];
          this.showGenreFilter = !params['genre'];

        }),
        switchMap(() => this.publicationService.getGeneralPublications(this.currentFilters)),
        tap((response) => {
          this.publications = response.body || [];
          this.processHeaders(response.headers);
        }),
        switchMap(() =>
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