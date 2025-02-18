import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { TranslatePipe } from "@ngx-translate/core";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import { Button } from "primeng/button";
import { InputGroup } from "primeng/inputgroup";
import { InputGroupAddon } from "primeng/inputgroupaddon";
import { InputText } from "primeng/inputtext";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import { switchMap, tap } from "rxjs";
import { BookModelService } from "../../core/services/bookmodel.service";
import { Pagination } from "../../core/models/pagination";
import { BookModel } from "../../core/models/bookModel.model";
import {FilterListComponent} from "../../shared/filter-list/filter-list.component";
import {BookModelCardComponent} from "../../shared/book-model-card/book-model-card.component";
import {PaginatorComponent} from "../../shared/paginator/paginator.component";
import {ScrollPanel} from "primeng/scrollpanel";
import {SortComponent} from "../../shared/sort/sort.component";

@Component({
  selector: 'app-book-model',
  imports: [
    NavbarComponent,
    TranslatePipe,
    Button,
    InputGroup,
    InputGroupAddon,
    InputText,
    ReactiveFormsModule,
    FormsModule,
    NgClass,
    NgIf,
    FilterListComponent,
    BookModelCardComponent,
    NgForOf,
    PaginatorComponent,
    ScrollPanel,
    RouterLink,
    SortComponent,
  ],
  templateUrl: './book-model.component.html',
  standalone: true,
  styleUrls: ['./book-model.component.css', '../book-home/book-home.component.css']
})
export class BookModelComponent implements OnInit {

  bookModels: BookModel[] = [];
  genreHeaders: Record<string, string> = {};
  showGenreFilter: boolean = true;
  currentFilters = {
    bookModelsUrl: 'api/book_models',
    genre: '',
    search: '',
    sort: '',
  };
  isSearchActive = false;
  lastSearchQuery: string | null = null;
  @ViewChild('searchInput') searchInput!: ElementRef;
  pagination: Pagination | null = null;
  uploadBookModelUrl: string = "/books/add";
  resetPaginatorNumber: boolean = false;


  constructor(private router: Router, private route: ActivatedRoute, private bms: BookModelService) {
  }

  ngOnInit() {
    this.getBookModels();
  }

  goBack() {
    this.router.navigate(['/books']);
  }

  search() {
    if (this.currentFilters.search) {
      this.isSearchActive = true;
      this.router.navigate([], { queryParams: { search: this.currentFilters.search }, queryParamsHandling: 'merge' });
    }
    else {
      this.router.navigate([], { queryParams: { search: null }, queryParamsHandling: 'merge' });
      this.isSearchActive = false;
    }

    this.lastSearchQuery = this.currentFilters.search;
    this.searchInput.nativeElement.blur();
  }

  onSearchChange() {}

  onBlur() {
    this.currentFilters.search = this.lastSearchQuery || '';
  }

  removeFilter(filterKey: keyof typeof this.currentFilters) {
    if (filterKey === 'genre') {
      this.currentFilters.genre = '';
    }

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { [filterKey]: null },
      queryParamsHandling: 'merge',
    });

    this.getBookModels();
  }

  onSortUpdate(sort: string) {
    this.currentFilters.sort = sort.replace(/^sort\./, '').toUpperCase().replace(/\./g, '_');
    this.resetPaginatorNumber = true;
    setTimeout(() => (this.resetPaginatorNumber = false), 300);
    this.getBookModels();
  }

  getBookModels(url: string | null = null) {
    this.route.queryParams.pipe(
        tap((params) => {
          this.currentFilters.search = params['search'] || '';
          this.currentFilters.sort = params['sort'] || '';
          this.currentFilters.genre = params['genre'] || '';
          this.showGenreFilter = !params['genre'];
        }),
        switchMap(() => {
          return url
              ? this.bms.getBookModelsByUrl(url)
              : this.bms.getBookModels({ ...this.currentFilters });
        }),
        tap((response) => {
          this.pagination = response.pagination;
          this.processHeaders(response.headers);
        })
    ).subscribe({
      next: (response) => {
        this.bookModels = response.bookModels;
      },
      error: (err) => {
        console.error("Error al obtener los modelos de libros:", err);
      }
    });
  }

  private processHeaders(headers: any) {
    const newGenreHeaders: Record<string, string> = {};

    headers.keys().forEach((key: string) => {
      const value = headers.get(key);

      if (value !== null) {
        if (key.startsWith("x-genre-")) {
          newGenreHeaders[key] = value;
        }
      }
    });

    this.genreHeaders = { ...newGenreHeaders };
  }

}

