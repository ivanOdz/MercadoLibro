import { Component } from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import { ButtonModule } from 'primeng/button';
import {Title} from "@angular/platform-browser";
import {SidebarComponent} from "./components/sidebar.component";
import {NgForOf} from "@angular/common";
import {Paginator, PaginatorState} from "primeng/paginator";
import {Steps} from "primeng/steps";
import {MenuItem} from "primeng/api";


@Component({
  selector: 'app-exchanges',
  templateUrl: `exchanges.component.html`,
  standalone: true,
  styleUrl: './exchanges.component.css',
  imports: [ButtonModule, SidebarComponent, NavbarComponent, NgForOf, Paginator, Steps]
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
  steps: MenuItem[] = [
    { label: 'Aceptado' },
    { label: 'Esperando confirmacion' },
    { label: 'Finalizado' }
  ];

    constructor(private titleService: Title) {
        this.titleService.setTitle(this.Title);
        this.first = 1;
    }


  confirmExchange(card: string) {

  }
}
