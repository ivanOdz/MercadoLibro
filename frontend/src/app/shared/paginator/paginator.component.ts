import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Pagination } from "../../core/models/pagination";
import { NgIf } from "@angular/common";
import { MatIconModule } from '@angular/material/icon';

@Component({
    selector: 'app-paginator',
    imports: [NgIf, MatIconModule],
    templateUrl: './paginator.component.html',
    standalone: true,
    styleUrl: './paginator.component.css'
})
export class PaginatorComponent implements OnChanges {
  @Input() fetchMethod!: (url: string, page: number) => void;
  @Input() pagination: Pagination | null = null;
  @Input() reset: boolean = false;
  page: number = 0;
  
  fetch(url: string | undefined, page: number) {
    if (url && this.fetchMethod) {
      
      const maxPage = this.pagination?.pages ?? 1;
      this.page = Math.max(0, Math.min(page, maxPage));
      
      this.fetchMethod(url, this.page);
    }
  }
  
  ngOnChanges(changes: SimpleChanges) {
    if (changes['reset'] && changes['reset'].currentValue === true) {
      this.page = 0;
	}
  }
}
