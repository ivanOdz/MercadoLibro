import {Component} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {Tab, TabList, TabPanel, TabPanels, Tabs} from "primeng/tabs";
import {NgForOf, NgIf} from "@angular/common";
import {Paginator, PaginatorState} from "primeng/paginator";
import {Rating} from "primeng/rating";
import {FormsModule} from "@angular/forms";
import {Button} from "primeng/button";
import {User} from "../../core/models/user.model";
import {ExchangeService} from "../../core/services/exchange.service";
import {UserService} from "../../core/services/user.service";
import {PublicationService} from "../../core/services/publication.service";
import {BookService} from "../../core/services/book.service";
import {BookmodelService} from "../../core/services/bookmodel.service";
import {AuthService} from "../../core/services/auth.service";
import {Router} from "@angular/router";
import {catchError, filter, forkJoin, Observable, of, switchMap} from "rxjs";
import {map} from "rxjs/operators";
import {Exchange} from "../../core/models/exchange.model";
import {BookData, ExchangeData} from "./exchanges.component";
import {ProgressSpinner} from "primeng/progressspinner";
import {Dialog} from "primeng/dialog";

@Component({
    selector: 'exchanges-requests',
    templateUrl: `requests.component.html`,
    standalone: true,
    styleUrl: './exchanges.component.css',
    imports: [
        SidebarComponent,
        NavbarComponent,
        Tabs,
        TabList,
        Tab,
        TabPanels,
        TabPanel,
        NgForOf,
        Paginator,
        Rating,
        FormsModule,
        Button,
        NgIf,
        ProgressSpinner,
        Dialog
    ]
})
export class RequestsComponent {
    loggedUser: User | null = null;

    offeredExchanges: ExchangeData[] = [];
    requestedExchanges: ExchangeData[] = [];


    constructor(private es: ExchangeService, private us: UserService, private ps: PublicationService,
                private bs: BookService, private bms: BookmodelService, private as: AuthService,
                private router: Router) {}

    ngOnInit(): void {
        this.isLoading = true;
        this.as.loggedUser$.subscribe((user: User | null) => {
            this.loggedUser = user;
            this.loadExchanges();
        });
    }

    // ##################  Api calls  ##################
    private loadExchanges(): void {
        this.requestedExchanges = [];
        this.offeredExchanges = [];
        this.isLoading = true;

        this.as.loggedUser$.pipe(
            filter((user: User | null) => !!user),
            switchMap((user: User) =>
                forkJoin({
                    offered: this.es.getExchangesOffers(user.exchanges, this.currentOfferedPage),
                    solicited: this.es.getSolicitedExchanges(user.exchanges, this.currentSolicitedPage)
                })
            ),
            switchMap(({ offered, solicited }) => {
                if (!offered.exchange.length && !solicited.exchange.length) {
                    console.warn("No se encontraron intercambios.");
                    this.isLoading = false;
                    return of({ requesterExchanges: [], offeredExchanges: [] });
                }

                return forkJoin({
                    requesterExchanges: this.processExchanges(offered.exchange),
                    offeredExchanges: this.processExchanges(solicited.exchange),
                });
            })
        ).subscribe(({ requesterExchanges, offeredExchanges }) => {
            this.requestedExchanges = requesterExchanges;
            this.offeredExchanges = offeredExchanges;
            this.isLoading = false;
            console.log("Requester Exchanges:", this.requestedExchanges);
            console.log("Offered Exchanges:", this.offeredExchanges);
        }, (error) => {
            this.isLoading = false;
            console.error("Error en la carga de intercambios:", error);
        });
    }

    /**
     * Procesa una lista de intercambios y obtiene los datos necesarios
     */
    private processExchanges(exchanges: Exchange[]): Observable<any[]> {
        if (exchanges.length === 0) return of([]);

        const exchangeRequests = exchanges.map((exchange) =>
            forkJoin({
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
                                offererBookModel: this.bms.getBookModel(offererBook.bookModel).pipe(catchError(() => of(null))),
                                requesterBookModel: this.bms.getBookModel(requesterBook.bookModel).pipe(catchError(() => of(null))),
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
            map((result) => result.filter((item) => item !== null)) // Eliminamos los nulos
        );
    }

    acceptExchange(){
        this.acceptExchangeDialogVisible = false;


        if (!this.exchangeData) {
            console.error("No se puede confirmar el intercambio sin datos.");
            return;
        }

        this.isLoading = true;
        this.es.acceptExchange(this.exchangeData.exchange.self, this.exchangeData.exchange.accept_code, null).subscribe(
            () => {
                console.log("Intercambio aceptado:", this.exchangeData?.exchange.self);
                this.loadExchanges();
            },
            (error) => console.error("Error al aceptar el intercambio:", error))
    }

    rejectExchange(){
        this.rejectExchangeDialogVisible = false;


        if (!this.exchangeData) {
            console.error("No se puede confirmar el intercambio sin datos.");
            return;
        }

        this.isLoading = true;
        this.es.acceptExchange(this.exchangeData.exchange.self, this.exchangeData.exchange.accept_code, null).subscribe(
            () => {
                console.log("Intercambio rechazado:", this.exchangeData?.exchange.self);
                this.loadExchanges();
            },
            (error) => console.error("Error al rechazar el intercambio:", error))
    }


    /***  Html functions  ***/

    selectedOffersCard: ExchangeData | null = null;
    selectedRequestsCard: ExchangeData | null = null;

    isLoading = true;

    selectRequesterCard(card: ExchangeData) {
        this.selectedRequestsCard = card;
    }

    selectOfferedCard(card: ExchangeData) {
        this.selectedOffersCard = card;
    }

    get selectedUserRatingRequested() {
        return this.isRequester(this.selectedRequestsCard) ? this.selectedRequestsCard?.offeredPub?.book?.owner?.ratingAverage : this.selectedRequestsCard?.requestedPub?.book?.owner?.ratingAverage;
    }

    get selectedUserRatingOffered() {
        return this.isRequester(this.selectedOffersCard) ? this.selectedOffersCard?.offeredPub?.book?.owner?.ratingAverage : this.selectedOffersCard?.requestedPub?.book?.owner?.ratingAverage;
    }

    redirectToPublications() {
        this.router.navigate(['/publications']);
    }

    isRequester(selectedCard: ExchangeData | null) {
        return this.loggedUser?.username == selectedCard?.requestedPub.book.owner?.username;
    }

    getBookImage(book: BookData) {
        return this.loggedUser === book.owner ?
            (book.image || 'assets/book.jpg') :
            (book.image || 'assets/book.jpg');
    }

    /*** Dialogs ***/

    acceptExchangeDialogVisible: boolean = false;
    rejectExchangeDialogVisible: boolean = false;

    exchangeData: ExchangeData | null = null;

    showRejectDialog(card: ExchangeData) {
        this.rejectExchangeDialogVisible = true;
        this.exchangeData = card;
    }

    showAcceptDialog(card: ExchangeData) {
        this.acceptExchangeDialogVisible = true;
        this.exchangeData = card;
    }

    /***  Pagination ***/

    rows: unknown;
    totalRecords: unknown;
    currentOfferedPage: number = 0;
    currentSolicitedPage: number = 0;












    /////////////////////////////////

    Title = "Requests";


    value: any;



    onRequestedPageChange($event: PaginatorState) {

    }

    onOfferredPageChange($event: PaginatorState) {

    }
}