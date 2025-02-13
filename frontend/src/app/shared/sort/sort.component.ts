import { Component} from '@angular/core';
import { TranslatePipe, TranslateService } from '@ngx-translate/core';
import { DropdownModule } from 'primeng/dropdown';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';  // Importa Router

@Component({
  selector: 'app-sort',
  templateUrl: './sort.component.html',
  standalone: true,
  imports: [
    DropdownModule,
    CommonModule,
    FormsModule,
    TranslatePipe
  ]
})
export class SortComponent {
  sortOptions: { label: string; value: string }[] = [];
  selectedSort: { label: string; value: string };

  constructor(
      private translate: TranslateService,
      private router: Router  // Inyecta Router
  ) {
    this.updateLabels();
    this.selectedSort = this.sortOptions[0];

    this.translate.onLangChange.subscribe(() => {
      this.updateLabels();
    });
  }

  updateLabels() {
    this.sortOptions = [
      { label: this.translate.instant('sort.publication.date.descending'), value: 'sort.publication.date.descending' },
      { label: this.translate.instant('sort.publication.date.ascending'), value: 'sort.publication.date.ascending' },
      { label: this.translate.instant('sort.rating.descending'), value: 'sort.rating.descending' },
      { label: this.translate.instant('sort.rating.ascending'), value: 'sort.rating.ascending' },
      { label: this.translate.instant('sort.book.name.ascending'), value: 'sort.book.name.ascending' },
      { label: this.translate.instant('sort.book.name.descending'), value: 'sort.book.name.descending' }
    ];
  }

  onSortChange() {
    this.router.navigate([], {
      queryParams: { sort: this.selectedSort.value },
      queryParamsHandling: 'merge'
    });
  }
}
