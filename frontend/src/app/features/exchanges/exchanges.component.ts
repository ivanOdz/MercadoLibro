import {Component, OnInit} from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import { ButtonModule } from 'primeng/button';
import {SidebarComponent} from "./components/sidebar.component";
import {NgClass, NgForOf, NgIf, NgOptimizedImage, NgStyle} from "@angular/common";
import {Paginator, PaginatorState} from "primeng/paginator";
import {Steps} from "primeng/steps";
import {MenuItem} from "primeng/api";
import {ConfirmDialogModule} from "primeng/confirmdialog";
import {Rating} from "primeng/rating";
import {FormsModule} from "@angular/forms";
import {Dialog} from "primeng/dialog";
import {InputText} from "primeng/inputtext";
import {Exchange} from "../../core/models/exchange.model";
import {ExchangeService} from "../../core/services/exchange.service";
import {UserService} from "../../core/services/user.service";
import {catchError, filter, forkJoin, of, switchMap, tap} from "rxjs";
import {User} from "../../core/models/user.model";
import {AuthService} from "../../core/services/auth.service";
import {Router} from "@angular/router";
import {BookModel} from "../../core/models/bookModel.model";
import {PublicationService} from "../../core/services/publication.service";
import {BookmodelService} from "../../core/services/bookmodel.service";
import {BookService} from "../../core/services/book.service";
import {Location} from "../../core/models/location.model";
import {map} from "rxjs/operators";
import {appConfig} from "../../app.config";
import {environment} from "../../../environments/environment";
import {ProgressSpinner} from "primeng/progressspinner";
import { ConfirmationService } from 'primeng/api';
import {Toast} from "primeng/toast";

export type message = { sender: number, message: string, date: Date };

export type ExchangeData = {exchange: Exchange, offeredPub: PublicationData, requestedPub: PublicationData/*, messages: Message[]*/};
export type PublicationData = {book: BookData, locations: Location[]};
export type BookData = {owner: User | null, image: string | null, model: BookModel | null};

@Component({
    selector: 'app-exchanges',
    templateUrl: `exchanges.component.html`,
    standalone: true,
    styleUrl: './exchanges.component.css',
    providers: [ConfirmationService],
    imports: [ButtonModule, SidebarComponent, NavbarComponent, NgForOf, Paginator,
        Steps, Rating, FormsModule, Dialog, InputText, NgIf, NgClass, NgOptimizedImage,
        ProgressSpinner, NgStyle, Toast, ConfirmDialogModule]
})
export class ExchangesComponent implements OnInit {
    loggedUser: User | null = null;

    activeExchanges: ExchangeData[] = [];


    constructor(private es: ExchangeService, private us: UserService, private ps: PublicationService,
                private bs: BookService, private bms: BookmodelService, private as: AuthService,
                private router: Router) {}

    ngOnInit(): void {
        this.isLoading = true;
        this.currentPage = 0;
        this.as.loggedUser$.subscribe((user: User | null) => {
            this.loggedUser = user;
            this.loadExchanges();
        });
    }

    // ##################  Api calls  ##################

    private loadExchanges(): void {
            this.activeExchanges = [];
            this.isLoading = true;


            this.as.loggedUser$.pipe(
            filter((user: User | null) => !!user),
                switchMap((user: User) =>
                    this.es.getActiveExchanges(user.exchanges, this.currentPage).pipe(
                        tap((response) => {
                            this.currentPage = response.pagination.currentPage;
                            this.totalRecords = response.pagination.totalAmount;
                            this.rows = response.pagination.totalAmount / response.pagination.maxPage;
                        }),
                        map(response => response.exchange),
                        catchError((error) => {
                            console.error("Error obteniendo intercambios:", error);
                            return of([]);
                        })
                    )
                )
                ,
                switchMap((exchanges: Exchange[]) => {
                    if (exchanges.length === 0) {
                        console.warn("No se encontraron intercambios.");
                        return of([]);
                }

                const exchangeRequests = exchanges.map((exchange) => forkJoin({
                        offererPub: this.ps.getPublication(exchange.offerer),
                        requesterPub: this.ps.getPublication(exchange.requester),
                    }).pipe(
                        switchMap(({ offererPub, requesterPub }) => {
                            if (!offererPub || !requesterPub) return of(null);

                            return forkJoin({
                                offererUser: this.us.getUser(offererPub.user).pipe(catchError(() => of(null))),
                                requesterUser: this.us.getUser(requesterPub.user).pipe(catchError(() => of(null))),
                                offererBook: this.bs.getBook(offererPub.book).pipe(catchError(() => of(null))),
                                requesterBook: this.bs.getBook(requesterPub.book).pipe(catchError(() => of(null))),
                                offererLocations: this.us.getLocationsInPublication(offererPub.locations).pipe(catchError(() => of([]))),
                                requesterLocations: this.us.getLocationsInPublication(requesterPub.locations).pipe(catchError(() => of([]))),
                            }).pipe(
                                switchMap(({ offererUser, requesterUser, offererBook, requesterBook, offererLocations, requesterLocations }) => {
                                    if (!offererBook || !requesterBook) return of(null);

                                    return forkJoin({
                                        offererBookModel: this.bms.getBookModel(offererBook.bookModel).pipe(
                                            tap((r) => console.log("Respuesta de la API de book model:", r)),
                                            catchError(() => of(null))),
                                        requesterBookModel: this.bms.getBookModel(requesterBook.bookModel).pipe(
                                            tap((r) => console.log("Respuesta de la API de book model:", r)),
                                            catchError(() => of(null))),
                                    }).pipe(
                                        map(({ offererBookModel, requesterBookModel }) => ({
                                            exchange,
                                            offeredPub: {
                                                book: {
                                                    owner: offererUser,
                                                    model: offererBookModel,
                                                    image: offererBook?.images?.[0] || null,
                                                },
                                                locations: offererLocations,
                                            },
                                            requestedPub: {
                                                book: {
                                                    owner: requesterUser,
                                                    model: requesterBookModel,
                                                    image: requesterBook?.images?.[0] || null,
                                                },
                                                locations: requesterLocations,
                                            }
                                        }))
                                    );
                                })
                            );
                        })
                    )
                );
                return forkJoin(exchangeRequests).pipe(
                    tap((result) => console.log("Intercambios procesados:", result)),
                    map((result) => result.filter((item) => item !== null)) // Eliminamos nulos
                );
            })
            ).subscribe(
                (activeExchanges) => {
                    this.activeExchanges = activeExchanges;
                    this.isLoading = false;
                    console.log("Intercambios cargados:", this.activeExchanges);
                },
                (error) => console.error("Error en la carga de intercambios:", error)
            );
    }


    confirmExchange(card: ExchangeData, requester: boolean) {
        this.confirmExchangeDialogVisible = false;

        if (!card.exchange) {
            console.error("No se puede confirmar el intercambio sin datos.");
            return;
        }

        this.isLoading = true;
        this.es.confirmExchange(card.exchange.self, card.exchange.accept_code, requester).subscribe(
            () => {
                console.log("Intercambio confirmado:", card.exchange.self);
                this.loadExchanges();
            },
            (error) => console.error("Error al confirmar el intercambio:", error))
    }


    /***  Html functions  ***/

    isLoading = true;

    selectedCard: ExchangeData | null = null;

    selectCard(cardText: ExchangeData) {
        this.selectedCard = cardText;
    }

    get selectedUserRating() {
        return this.isRequester(this.selectedCard) ? this.selectedCard?.offeredPub?.book?.owner?.ratingAverage : this.selectedCard?.requestedPub?.book?.owner?.ratingAverage;
    }

    redirectToPublications() {
        this.router.navigate(['/publications']);
    }

    isRequester(selectedCard: ExchangeData | null) {
        return this.loggedUser === selectedCard?.requestedPub.book.owner;
    }

    getBookImage(book: BookData) {
        return this.loggedUser === book.owner ?
            (book.image || 'assets/book.jpg') :
            (book.image || 'assets/book.jpg');
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

    rows: unknown;
    totalRecords: unknown;
    currentPage: number = 0;
    onPageChange($event: PaginatorState) {
        this.currentPage = $event.page || 0;
    }


    Title = "Intercambios activos";


    monthNames: string[] = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];


    displayModal: boolean = false;

    private changeDetectorRef: any;

    steps: MenuItem[] = [
        { label: 'Aceptado' },
        { label: 'Esperando confirmacion' },
        { label: 'Finalizado' }
    ];
    value: any;
    newMessage: any;
    messages: message[] = [ { sender: 1, message: 'Hello', date: new Date('2025-02-04') }, { sender: 2, message: 'Hi', date: new Date()} ];
    lastDate: Date = new Date('2025-02-04');




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



    protected readonly environment = environment;








}
