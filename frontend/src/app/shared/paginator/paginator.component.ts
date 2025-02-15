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
  @Input() fetchMethod!: (url: string) => void;
  @Input() pagination: Pagination | null = null;
  page: number = 1;
  
  fetch(url: string | undefined, page: number) {
    if (url && this.fetchMethod) {

      if (this.pagination && page > this.pagination!.pages)
      { this.page = this.pagination?.pages || 1; }
	  else if (page < 1)
	  { this.page = 1; }
      else
	  { this.page = page; }
	  
      this.fetchMethod(url);
    }
  }

}
