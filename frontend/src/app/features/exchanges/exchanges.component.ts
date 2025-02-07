import {Component, NgIterable, OnInit} from '@angular/core';
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
import {Exchange} from "../../core/models/exchange.model";
import {ExchangesService} from "../../core/services/exchanges.service";
import {UserService} from "../../core/services/user.service";
import {Observable} from "rxjs";
import {User} from "../../core/models/user.model";
import {AuthService} from "../../core/services/auth.service";
import {Router} from "@angular/router";

type message = { sender: number, message: string, date: Date };

@Component({
  selector: 'app-exchanges',
  templateUrl: `exchanges.component.html`,
  standalone: true,
  styleUrl: './exchanges.component.css',
  imports: [ButtonModule, SidebarComponent, NavbarComponent, NgForOf, Paginator, Steps, Rating, FormsModule, Dialog, InputText, NgIf, NgClass]
})
export class ExchangesComponent implements OnInit {
  activeExchanges: Exchange[] = [];
  constructor(private es: ExchangesService, private as: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.loadExchanges();
  }

  // ##################  Api calls  ##################

  private loadExchanges(): void {
    this.as.loggedUser$.subscribe((user: User | null) => {
      if (user) {
        this.es.getActiveExchanges(user.exchanges, this.currentPage).subscribe((exchanges: Exchange[]) => {
          this.activeExchanges = exchanges;
        });
      }
    });
    console.log(this.activeExchanges.length);
  }

  //##################  Html functions  ##################

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
  currentPage: number = 0;
  steps: MenuItem[] = [
    { label: 'Aceptado' },
    { label: 'Esperando confirmacion' },
    { label: 'Finalizado' }
  ];
  value: any;
  newMessage: any;
  messages: message[] = [ { sender: 1, message: 'Hello', date: new Date('2025-02-04') }, { sender: 2, message: 'Hi', date: new Date()} ];
  lastDate: Date = new Date('2025-02-04');



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

  redirectToPublications() {
    this.router.navigate(['/publications']);
  }
}
