import {Component, NgIterable} from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import { ButtonModule } from 'primeng/button';
import {Title} from "@angular/platform-browser";
import {SidebarComponent} from "./components/sidebar.component";
import {DatePipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {Paginator, PaginatorState} from "primeng/paginator";
import {Steps} from "primeng/steps";
import {MenuItem} from "primeng/api";
import {Rating} from "primeng/rating";
import {FormsModule} from "@angular/forms";
import {Dialog} from "primeng/dialog";
import {InputText} from "primeng/inputtext";

type message = { sender: number, message: string, date: Date };

@Component({
  selector: 'app-exchanges',
  templateUrl: `exchanges.component.html`,
  standalone: true,
  styleUrl: './exchanges.component.css',
  imports: [ButtonModule, SidebarComponent, NavbarComponent, NgForOf, Paginator, Steps, Rating, FormsModule, Dialog, InputText, NgIf, NgClass]
})
export class ExchangesComponent {

  Title = "Intercambios activos";

  selectedCard: string | null = null;

  monthNames: string[] = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];

  displayModal: boolean = false;
  private changeDetectorRef: any;



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
  value: any;
  newMessage: any;
  messages: message[] = [ { sender: 1, message: 'Hello', date: new Date('2025-02-04') }, { sender: 2, message: 'Hi', date: new Date()} ];
  lastDate: Date = new Date('2025-02-04');

    constructor(private titleService: Title) {
        this.titleService.setTitle(this.Title);
        this.first = 1;
    }


  confirmExchange(card: string) {

  }

  openChat() {
    this.displayModal = true;
  }

  sendMessage() {
    if (this.newMessage.trim()) {
      this.messages.push(this.newMessage);
      this.newMessage = '';
      this.changeDetectorRef.detectChanges();
    }
  }

  getMonthName(month: number) {
    return this.monthNames[month - 1];
  }
}
