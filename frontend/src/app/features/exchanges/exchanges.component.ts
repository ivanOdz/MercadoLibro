import { Component } from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import { ButtonModule } from 'primeng/button';
import {Title} from "@angular/platform-browser";
import {SidebarComponent} from "./components/sidebar.component";
import {NgForOf} from "@angular/common";


@Component({
  selector: 'app-exchanges',
  templateUrl: `exchanges.component.html`,
  standalone: true,
  styleUrl: './exchanges.component.css',
  imports: [ButtonModule, SidebarComponent, NavbarComponent, NgForOf]
})
export class ExchangesComponent {

  Title = "Intercambios activos";

  selectedCard: string | null = null;

  selectCard(cardText: string) {
    this.selectedCard = cardText;
  }


}
