import { Component, Input } from '@angular/core';
import { Pagination } from "../../../core/models/pagination";
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

  fetch(url: string | undefined) {
    if (url && this.fetchMethod) {
      this.fetchMethod(url);
    }
  }

}
