import {Component, OnInit} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {NgForOf, NgIf} from "@angular/common";
import {Tab, TabList, TabPanel, TabPanels, Tabs} from "primeng/tabs";
import {Paginator, PaginatorState} from "primeng/paginator";
import {Rating} from "primeng/rating";
import {FormsModule} from "@angular/forms";
import {Button} from "primeng/button";
import {Textarea} from "primeng/textarea";
import {Popover} from "primeng/popover";
import {User} from "../../core/models/user.model";
import {BookData, ExchangeData} from "./exchanges.component";
import {ExchangeService} from "../../core/services/exchange.service";
import {UserService} from "../../core/services/user.service";
import {PublicationService} from "../../core/services/publication.service";
import {BookService} from "../../core/services/book.service";
import {BookmodelService} from "../../core/services/bookmodel.service";
import {AuthService} from "../../core/services/auth.service";
import {Router} from "@angular/router";
import {catchError, filter, forkJoin, Observable, of, switchMap} from "rxjs";
import {Exchange} from "../../core/models/exchange.model";
import {map} from "rxjs/operators";
import {ProgressSpinner} from "primeng/progressspinner";

@Component({
    selector: 'exchanges-history',
    templateUrl: `history.component.html`,
    standalone: true,
    styleUrl: './exchanges.component.css',
    imports: [
        SidebarComponent,
        NavbarComponent,
        NgForOf,
        Tab,
        TabList,
        TabPanel,
        TabPanels,
        Tabs,
        Paginator,
        Rating,
        FormsModule,
        Button,
        Textarea,
        Popover,
        ProgressSpinner,
        NgIf
    ]
})
export class HistoryComponent implements OnInit {

    loggedUser: User | null = null;

    completedExchanges: ExchangeData[] = [];
    rejectedExchanges: ExchangeData[] = [];


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



    private loadExchanges(): void {
        this.rejectedExchanges = [];
        this.completedExchanges = [];
        this.isLoading = true;

        this.as.loggedUser$.pipe(
            filter((user: User | null) => !!user),
            switchMap((user: User) =>
                forkJoin({
                    completed: this.es.getCompletedExchanges(user.exchanges, this.currentCompletedPage),
                    rejected: this.es.getRejectedExchanges(user.exchanges, this.currentRejectedPage)
                })
            ),
            switchMap(({ completed, rejected }) => {
                if (!completed.exchange.length && !rejected.exchange.length) {
                    console.warn("No se encontraron intercambios.");
                    this.isLoading = false;
                    return of({ rejectedExchanges: [], completedExchanges: [] });
                }

                return forkJoin({
                    rejectedExchanges: this.processExchanges(completed.exchange),
                    completedExchanges: this.processExchanges(rejected.exchange),
                });
            })
        ).subscribe(({ rejectedExchanges, completedExchanges }) => {
            this.rejectedExchanges = rejectedExchanges;
            this.completedExchanges = completedExchanges;
            this.isLoading = false;
            console.log("Rejected Exchanges:", this.rejectedExchanges);
            console.log("Completed Exchanges:", this.completedExchanges);
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
                                    },
                                    messages: []
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
    /***  Html functions  ***/

    Title = "History";

    isLoading = true;


    selectedCompletedCard: ExchangeData | null = null;
    selectedRejectedCard: ExchangeData | null = null;

    selectRejectedCard(card: ExchangeData) {
        this.selectedRejectedCard = card;
    }

    selectCompletedCard(card: ExchangeData) {
        this.selectedCompletedCard = card;
    }

    get selectedUserRatingRejected() {
        return this.isRequester(this.selectedRejectedCard) ? this.selectedRejectedCard?.offeredPub?.book?.owner?.ratingAverage : this.selectedRejectedCard?.requestedPub?.book?.owner?.ratingAverage;
    }

    get selectedUserRatingCompleted() {
        return this.isRequester(this.selectedCompletedCard) ? this.selectedCompletedCard?.offeredPub?.book?.owner?.ratingAverage : this.selectedCompletedCard?.requestedPub?.book?.owner?.ratingAverage;
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

    /***  Pagination ***/

    rows: unknown;
    totalRecords: unknown;
    currentCompletedPage: number = 0;
    currentRejectedPage: number = 0;

















    /////////////////////////////////////


    showContent = false;

    reviewText: string = '';
    reviewValue: number = 0;

    toggleReviewContent() {
        this.showContent = !this.showContent;
    }

    onPageChange($event: any) {
        console.log($event);
    }

    confirmReview() {

    }

    onCompletedPageChange($event: PaginatorState) {

    }

    onRejectedPageChange($event: PaginatorState) {

    }
}