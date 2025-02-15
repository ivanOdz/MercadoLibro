import {Component, OnInit} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";
import {NavbarComponent} from "../../shared/navbar/navbar.component";
import {NgForOf, NgIf} from "@angular/common";
import {Tab, TabList, TabPanel, TabPanels, Tabs} from "primeng/tabs";
import {Rating} from "primeng/rating";
import {FormsModule} from "@angular/forms";
import {Button} from "primeng/button";
import {Textarea} from "primeng/textarea";
import {Popover} from "primeng/popover";
import {User} from "../../core/models/user.model";
import {ExchangeService} from "../../core/services/exchange.service";
import {UserService} from "../../core/services/user.service";
import {PublicationService} from "../../core/services/publication.service";
import {BookService} from "../../core/services/book.service";
import {BookModelService} from "../../core/services/bookmodel.service";
import {AuthService} from "../../core/services/auth.service";
import {Router} from "@angular/router";
import { filter, forkJoin, of, switchMap} from "rxjs";
import {ProgressSpinner} from "primeng/progressspinner";
import {BookData, ExchangeData} from "../../core/models/types";
import { PaginatorComponent } from "../../shared/paginator/paginator.component";
import {Pagination} from "../../core/models/pagination";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {ScrollPanelModule} from "primeng/scrollpanel";

@Component({
    selector: 'exchanges-history',
    templateUrl: 'history.component.html',
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
        Rating,
        FormsModule,
        Button,
        Textarea,
        Popover,
        ProgressSpinner,
        NgIf,
        PaginatorComponent,
        TranslatePipe,
        ScrollPanelModule
    ]
})
export class HistoryComponent implements OnInit {

    loggedUser: User | null = null;

    completedExchanges: ExchangeData[] = [];
    rejectedExchanges: ExchangeData[] = [];


    constructor(private es: ExchangeService, private us: UserService, private ps: PublicationService,
                private bs: BookService, private bms: BookModelService, private as: AuthService,
                private router: Router, private translate: TranslateService) {}

    ngOnInit(): void {
        this.isLoading = true;
        this.translate.onLangChange.subscribe(() => this.loadHistoryVariablesNames());
        this.loadHistoryVariablesNames();
        this.as.loggedUser$.subscribe((user: User | null) => {
            this.loggedUser = user;
            this.loadExchanges();
        });
    }



    private loadExchanges(url: string | null = null, isCompleted: boolean | null = null): void {
        this.rejectedExchanges = [];
        this.completedExchanges = [];
        this.isLoading = true;

        this.as.loggedUser$.pipe(
            filter((user: User | null) => !!user),
            switchMap((user: User) => {
                let requests;

                if (url !== null && isCompleted === true) {
                    requests = forkJoin({
                        completed: this.es.getExchangesByUrl(url),
                        rejected: this.es.getSolicitedExchanges(user.exchanges, this.currentRejectedPage)
                    });
                } else if (url !== null && isCompleted === false) {
                    requests = forkJoin({
                        completed: this.es.getExchangesOffers(user.exchanges, this.currentCompletedPage),
                        rejected: this.es.getExchangesByUrl(url)
                    });
                } else {
                    requests = forkJoin({
                        completed: this.es.getCompletedExchanges(user.exchanges, this.currentCompletedPage),
                        rejected: this.es.getRejectedExchanges(user.exchanges, this.currentRejectedPage)
                    });
                }

                return requests;
            }),
            switchMap(({ completed, rejected }) => {
                if (!completed.exchange.length && !rejected.exchange.length) {
                    console.warn("No se encontraron intercambios.");
                    this.isLoading = false;
                    return of({ rejectedExchanges: [], completedExchanges: [] });
                }
                this.paginationCompleted = completed.pagination;
                this.paginationRejected = rejected.pagination
                return forkJoin({
                    rejectedExchanges: this.es.processExchanges(rejected.exchange),
                    completedExchanges: this.es.processExchanges(completed.exchange),
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

    addUserReview($event: any, op:any) {
        if (!this.reviewText || !this.reviewValue) return;
        console.log("en add review self" + this.userToReview?.self);
        console.log("en add review" + this.userToReview);

        this.us.getUser(this.userToReview?.self).subscribe((user: User) => {
            this.us.postReview(user.reviews, this.selectedCompletedCard?.exchange.self , this.reviewValue, this.reviewText).subscribe(() => {
                console.log("Reseña creada exitosamente");
                this.reviewText = '';
                this.reviewValue = 0;
                op.toggle($event);
            });
        });
    }


    /***  Html functions  ***/

    Title = "";

    isLoading = true;


    selectedCompletedCard: ExchangeData | null = null;
    selectedRejectedCard: ExchangeData | null = null;

    selectRejectedCard(card: ExchangeData) {
        this.selectedRejectedCard = card;
    }

    selectCompletedCard(card: ExchangeData) {
        this.selectedCompletedCard = card;
    }

    selectedUserRatingRejected() {
        return this.isRequester(this.selectedRejectedCard) ? this.selectedRejectedCard?.offeredPub?.book?.owner?.ratingAverage : this.selectedRejectedCard?.requestedPub?.book?.owner?.ratingAverage;
    }

    selectedUserRatingCompleted() {
        return this.isRequester(this.selectedCompletedCard) ? this.selectedCompletedCard?.offeredPub?.book?.owner?.ratingAverage : this.selectedCompletedCard?.requestedPub?.book?.owner?.ratingAverage;
    }

    redirectToPublications() {
        this.router.navigate(['/publications']);
    }

    isRequester(selectedCard: ExchangeData | null) {
        return this.loggedUser?.username == selectedCard?.requestedPub.book?.owner?.username;
    }

    getBookImage(book: BookData | null) {
        return book?.bookModel?.cover || 'assets/book.jpg';
    }

    reviewText: string = '';
    reviewValue: number = 0;

    userToReview: User | null | undefined = null;

    toggleReviewContent($event:any,op:any) {
        this.userToReview = this.isRequester(this.selectedCompletedCard) ? this.selectedCompletedCard?.offeredPub?.book?.owner : this.selectedCompletedCard?.requestedPub?.book?.owner;
        console.log("en toggle review" + this.userToReview);
        op.toggle($event)
    }


    loadHistoryVariablesNames() {
        this.Title = this.translate.instant('EXCHANGES.HISTORY_TITLE');
    }


    /***  Pagination ***/

    paginationCompleted: Pagination | null = null;
    paginationRejected: Pagination | null = null;

    currentCompletedPage: number = 0;
    currentRejectedPage: number = 0;

    getCompleted(url: string) {
        this.loadExchanges(url, true);
    }

    getRejected(url: string) {
        this.loadExchanges(url, false);
    }

}