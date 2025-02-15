import { Component, Input } from '@angular/core';
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
export class PaginatorComponent {
  @Input() fetchMethod!: (url: string, page: number) => void;
  @Input() pagination: Pagination | null = null;
  page: number = 1;
  
  fetch(url: string | undefined, page: number) {
    if (url && this.fetchMethod) {
      
      const maxPage = this.pagination?.pages ?? 1;
      this.page = Math.max(1, Math.min(page, maxPage));
      
      this.fetchMethod(url, this.page);
    }
  }

}
