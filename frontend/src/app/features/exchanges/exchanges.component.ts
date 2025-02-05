import { Component } from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import { ButtonModule } from 'primeng/button';
import {Title} from "@angular/platform-browser";
import {SidebarComponent} from "./components/sidebar.component";
import {NgForOf} from "@angular/common";
import {Paginator, PaginatorState} from "primeng/paginator";


@Component({
  selector: 'app-exchanges',
  templateUrl: `exchanges.component.html`,
  standalone: true,
  styleUrl: './exchanges.component.css',
  imports: [ButtonModule, SidebarComponent, NavbarComponent, NgForOf, Paginator]
})
export class ExchangesComponent {

  Title = "Intercambios activos";

  selectedCard: string | null = null;

  selectCard(cardText: string) {
    this.selectedCard = cardText;
  }


  onPageChange($event: PaginatorState) {

  }

  rows: unknown;
  totalRecords: unknown;
  first: number;


    constructor(private titleService: Title) {
        this.titleService.setTitle(this.Title);
        this.first = 1;
    }
}
