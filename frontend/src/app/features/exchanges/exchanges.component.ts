import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NavbarComponent} from "../../shared/navbar/navbar.component";
import { ButtonModule } from 'primeng/button';
import {SidebarComponent} from "./components/sidebar.component";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {Steps} from "primeng/steps";
import {MenuItem} from "primeng/api";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {Rating} from "primeng/rating";
import {FormsModule} from "@angular/forms";
import { ScrollPanelModule } from 'primeng/scrollpanel';
import {Dialog} from "primeng/dialog";
import {InputText} from "primeng/inputtext";
import {ExchangeService} from "../../core/services/exchange.service";
import {catchError, filter, of, switchMap, tap} from "rxjs";
import {User} from "../../core/models/user.model";
import {AuthService} from "../../core/services/auth.service";
import {Router} from "@angular/router";
import {environment} from "../../../environments/environment";
import {ProgressSpinner} from "primeng/progressspinner";
import { ConfirmationService } from 'primeng/api';
import {BookData, ExchangeData} from "../../core/models/types";
import { PaginatorComponent } from "../../shared/paginator/paginator.component";
import {Pagination} from "../../core/models/pagination";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {map} from "rxjs/operators";
import {SnackbarService} from "../../core/services/snackbar.service";

@Component({
    selector: 'app-exchanges',
    templateUrl: 'exchanges.component.html',
    standalone: true,
    styleUrl: './exchanges.component.css',
    providers: [ConfirmationService],
    imports: [ButtonModule, SidebarComponent, NavbarComponent, NgForOf,
        Steps, Rating, FormsModule, Dialog, InputText, NgIf, NgClass,
        ProgressSpinner, ConfirmDialogModule, PaginatorComponent, TranslatePipe,
        ScrollPanelModule]
})
export class ExchangesComponent implements OnInit {
    loggedUser: User | null = null;

    activeExchanges: ExchangeData[] = [];


    constructor(private es: ExchangeService, private as: AuthService,
                private router: Router, private changeDetectorRef: ChangeDetectorRef, private translate: TranslateService, private snackBarService: SnackbarService) {}

    ngOnInit(): void {
        this.isLoading = true;
        this.currentPage = 0;
        this.translate.onLangChange.subscribe(() => this.loadActiveVariablesNames());
        this.loadActiveVariablesNames();
        this.as.loggedUser$.subscribe((user: User | null) => {
            this.loggedUser = user;
            this.loadExchanges();
        });
    }

    // ##################  Api calls  ##################

    private loadExchanges(url: string | null = null): void {
        this.activeExchanges = [];
        this.isLoading = true;

        this.as.loggedUser$.pipe(
            filter((user: User | null) => !!user),
            switchMap((user: User) => {
                const exchangeObservable = url
                    ? this.es.getExchangesByUrl(url)
                    : this.es.getActiveExchanges(user.exchanges, this.currentPage);

                return exchangeObservable.pipe(
                    tap((response) => {
                        this.pagination = response.pagination;
                    }),
                    map(response => response.exchange),
                    catchError((error) => {
                        this.snackBarService.showError('ERROR.GET_EXCHANGES');
                        return of([]); // Retorna un array vacío si hay error
                    })
                );
            }),
            switchMap((exchanges) => {
                return this.es.processExchanges(exchanges);
            })
        ).subscribe(
            (activeExchanges) => {
                this.activeExchanges = activeExchanges;
                this.isLoading = false;
            },
            (error) => {
                this.isLoading = false;
                this.snackBarService.showError('ERROR.GET_EXCHANGES');
            }
        );
    }






    confirmExchange(card: ExchangeData, requester: boolean) {
        this.confirmExchangeDialogVisible = false;

        if (!card.exchange) {
            this.snackBarService.showError('ERROR.UPDATE_EXCHANGE');
            return;
        }

        this.isLoading = true;
        this.es.confirmExchange(card.exchange.self, card.exchange.accept_code, requester).subscribe(
            () => {
                this.es.getExchange(card.exchange.self).subscribe(
                    (exchangeData) => {
                        if (exchangeData.requester_received && exchangeData.offerer_received) {
                            this.router.navigate(['/exchanges/history'], { queryParams: { selectedTab: 0 } });
                        } else {
                            this.loadExchanges(); // stay in the same page
                        }
                    },
                    (error) => this.snackBarService.showError('ERROR.GET_EXCHANGE'));
            },
            (error) => this.snackBarService.showError('ERROR.CONFIRM_EXCHANGE'));

    }

    sendMessage() {
        if (this.newMessage.trim() && this.selectedCard?.exchange.chat) {
            this.es.sendMessage(this.selectedCard?.exchange.chat, this.isRequester(this.selectedCard) ? this.selectedCard.requestedPub.book?.owner?.self : this.selectedCard.offeredPub.book?.owner?.self, this.newMessage).subscribe(
                (messageUrn) => {
                    console.log("Mensaje enviado:", messageUrn);
                    this.es.getMessage(messageUrn).subscribe(
                        (message) => {
                            this.addMessage(message);
                        },
                        (error) => this.snackBarService.showError('ERROR.GET_MESSAGE'));
                },
                (error) => this.snackBarService.showError('ERROR.SEND_MESSAGE'));


            this.newMessage = '';
            this.changeDetectorRef.detectChanges();
        }
    }


    /***  Html functions  ***/

    isLoading = true;

    selectedCard: ExchangeData | null = null;

    selectCard(cardText: ExchangeData) {
        this.selectedCard = cardText;
    }

    selectedUserRating() {
        return this.isRequester(this.selectedCard) ? this.selectedCard?.offeredPub?.book?.owner?.ratingAverage : this.selectedCard?.requestedPub?.book?.owner?.ratingAverage;
    }

    redirectToPublications() {
        this.router.navigate(['/publications']);
    }

    isRequester(selectedCard: ExchangeData | null) {
        return this.loggedUser?.username == selectedCard?.requestedPub?.book?.owner?.username;
    }

    addMessage(newMessage: any) {
        this.selectedCard?.messages.push(newMessage);
        this.changeDetectorRef.detectChanges();
    }

    trackByMessage(index: number, message: any): any {
        return message.id;
    }

    isSameDay(date1: any, date2: any): boolean {
        const date1Obj = new Date(date1);
        const date2Obj = new Date(date2);
        return (
            date1Obj.getFullYear() === date2Obj.getFullYear() &&
            date1Obj.getMonth() === date2Obj.getMonth() &&
            date1Obj.getDate() === date2Obj.getDate()
        );
    }

    getDay(date: any): string {
        const dateObj = new Date(date);
        return dateObj.getDay().toString();
    }

    getMonth(date: any) {
        const dateObj = new Date(date);
        return dateObj.getMonth();
    }

    getYear(date: any): string {
        const dateObj = new Date(date);
        return dateObj.getFullYear().toString();
    }

    getHour(date: any): string {
        const dateObj = new Date(date);
        return (dateObj.getHours() < 10 ? '0' : '') + dateObj.getHours().toString();
    }

    getMinute(date: any): string {
        const dateObj = new Date(date);
        return (dateObj.getMinutes() < 10 ? '0' : '') + dateObj.getMinutes().toString() ;
    }

    loadActiveVariablesNames() {
        this.Title = this.translate.instant('EXCHANGES.ACTIVE_TITLE');
        this.steps = [
            { label: this.translate.instant('EXCHANGES.ACCEPTED') },
            { label: this.translate.instant('EXCHANGES.AWAITING_CONFIRMATION') },
            { label: this.translate.instant('EXCHANGES.COMPLETED') }
        ]
    }

    getImages(bookImages: string[] | null = null): string[] {
        return bookImages || [this.getDefaultImage()];
    }

    getDefaultImage() {
        return `${environment.production? 'http://pawserver.it.itba.edu.ar/paw-2024b-09'  : 'http://localhost:8080'}/assets/book.jpg`;
    }

    getCover(book: BookData | null = null) {
        return book?.bookModel?.cover ||
            this.getImages(book?.images)[0] || this.getDefaultImage();
    }


    /*** Dialogs ***/

    confirmExchangeDialogVisible: boolean = false;

    confirmData: {card: ExchangeData, requester: boolean} = {card: null as unknown as ExchangeData, requester: false};


    showConfirmExchangeDialog(card: ExchangeData, b: boolean) {
        this.confirmExchangeDialogVisible = true;
        this.confirmData.card = card;
        this.confirmData.requester = b;
    }


    /***  Pagination ***/

    pagination: Pagination | null = null;
    currentPage: number = 0;

    getActive(url: string) {
        this.loadExchanges(url);
    }


    Title = "Intercambios activos";


    monthNames: string[] = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];


    displayModal: boolean = false;

    steps: MenuItem[] = [];
    value: any;
    newMessage: any;



    openChat() {
        this.displayModal = true;
    }


    getMonthName(month: number) {
        return this.monthNames[month - 1];
    }



    protected readonly environment = environment;


    isValidDate(date: any): boolean {
        return date instanceof Date && !isNaN(date.getTime());
    }

    getFormattedDate(time: Date) {
        const date = new Date(time);
        switch (this.translate.currentLang) {
            case 'es':
                return date.toLocaleDateString('es-ES', { day: 'numeric', month: 'long' });
            case 'en':
                return date.toLocaleDateString('en-GB', { day: 'numeric', month: 'long' }); // en-GB para evitar coma en inglés
        }
        return "";
    }

}
