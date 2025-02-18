import {Component, OnInit} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";
import {NavbarComponent} from "../../shared/navbar/navbar.component";
import {Tab, TabList, TabPanel, TabPanels, Tabs} from "primeng/tabs";
import {NgForOf, NgIf} from "@angular/common";
import {Rating} from "primeng/rating";
import {FormsModule} from "@angular/forms";
import {Button} from "primeng/button";
import {User} from "../../core/models/user.model";
import {ExchangeService} from "../../core/services/exchange.service";
import {UserService} from "../../core/services/user.service";
import {PublicationService} from "../../core/services/publication.service";
import {BookService} from "../../core/services/book.service";
import {BookModelService} from "../../core/services/bookmodel.service";
import {AuthService} from "../../core/services/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {filter, forkJoin, of, switchMap} from "rxjs";
import {ProgressSpinner} from "primeng/progressspinner";
import {Dialog} from "primeng/dialog";
import {BookData, ExchangeData} from "../../core/models/types";
import {Pagination} from "../../core/models/pagination";
import {PaginatorComponent} from "../../shared/paginator/paginator.component";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {ScrollPanelModule} from "primeng/scrollpanel";
import {environment} from "../../../environments/environment";
import {SnackbarService} from "../../core/services/snackbar.service";

@Component({
    selector: 'exchanges-requests',
    templateUrl: 'requests.component.html',
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
        Rating,
        FormsModule,
        Button,
        NgIf,
        ProgressSpinner,
        Dialog,
        PaginatorComponent,
        TranslatePipe,
        ScrollPanelModule
    ]
})
export class RequestsComponent  implements OnInit {
    loggedUser: User | null = null;

    offeredExchanges: ExchangeData[] = [];
    requestedExchanges: ExchangeData[] = [];


    constructor(private es: ExchangeService, private us: UserService, private ps: PublicationService,
                private bs: BookService, private bms: BookModelService, private as: AuthService,
                private router: Router, private translate: TranslateService,private route: ActivatedRoute, private snackBarService: SnackbarService) {}

    ngOnInit(): void {
        this.isLoading = true;
        this.translate.onLangChange.subscribe(() => this.loadRequestVariablesNames());
        this.loadRequestVariablesNames();

        this.route.queryParams.subscribe((params) => {
            if(params['selectedTab'] !== undefined){
                this.value = +params['selectedTab'];
            }
        })

        this.as.loggedUser$.subscribe((user: User | null) => {
            this.loggedUser = user;
            this.loadExchanges();
        });
    }

    // ##################  Api calls  ##################
    private loadExchanges(url: string | null = null, isOffer: boolean | null = null): void {
        this.requestedExchanges = [];
        this.offeredExchanges = [];
        this.isLoading = true;

        this.as.loggedUser$.pipe(
            filter((user: User | null) => !!user),
            switchMap((user: User) => {
                let requests;

                if (isOffer != null && url != null) {
                    if (isOffer) {
                        requests = forkJoin({
                            offered: this.es.getExchangesByUrl(url),
                            solicited: this.es.getSolicitedExchanges(user.exchanges, this.currentSolicitedPage)
                        });
                    } else {
                        requests = forkJoin({
                            offered: this.es.getExchangesOffers(user.exchanges, this.currentOfferedPage),
                            solicited: this.es.getExchangesByUrl(url)
                        });
                    }
                }else  {
                    requests = forkJoin({
                        offered: this.es.getExchangesOffers(user.exchanges, this.currentOfferedPage),
                        solicited: this.es.getSolicitedExchanges(user.exchanges, this.currentSolicitedPage)
                    });
                }
                return requests;
            }),
            switchMap(({ offered, solicited }) => {
                if (!offered.exchange.length && !solicited.exchange.length) {
                    this.isLoading = false;
                    return of({ requesterExchanges: [], offeredExchanges: [] });
                }
                this.paginationOffered = offered.pagination;
                this.paginationSolicited = solicited.pagination
                return forkJoin({
                    requesterExchanges: this.es.processExchanges(solicited.exchange),
                    offeredExchanges: this.es.processExchanges(offered.exchange),
                });
            })
        ).subscribe(({ requesterExchanges, offeredExchanges }) => {
            this.requestedExchanges = requesterExchanges;
            this.offeredExchanges = offeredExchanges;
            this.isLoading = false;
        }, (error) => {
            this.isLoading = false;
            this.snackBarService.showError('ERROR.GET_EXCHANGES');
        });
    }

    acceptExchange(){
        this.acceptExchangeDialogVisible = false;


        if (!this.exchangeData) {
            this.snackBarService.showError('ERROR.CONFIRM_EXCHANGE');
            return;
        }

        this.isLoading = true;
        this.es.acceptExchange(this.exchangeData.exchange.self, this.exchangeData.exchange.accept_code, null).subscribe(
            () => {
                this.router.navigate(['/exchanges']);
            },
            (error) => this.snackBarService.showError('ERROR.CONFIRM_EXCHANGE'))
    }

    rejectExchange(){
        this.rejectExchangeDialogVisible = false;


        if (!this.exchangeData) {
            this.snackBarService.showError('ERROR.UPDATE_EXCHANGE')
            return;
        }

        this.isLoading = true;
        this.es.rejectExchange(this.exchangeData.exchange.self, this.exchangeData.exchange.accept_code, null).subscribe(
            () => {
                this.router.navigate(['/exchanges/history'], { queryParams: { selectedTab: 1 } });
            },
            (error) =>  this.snackBarService.showError('ERROR.REJECT_EXCHANGE'))
    }


    /***  Html functions  ***/

    Title = "";

    value: number = 0;

    selectedOffersCard: ExchangeData | null = null;
    selectedRequestsCard: ExchangeData | null = null;

    isLoading = true;

    selectRequesterCard(card: ExchangeData) {
        this.selectedRequestsCard = card;
    }

    selectOfferedCard(card: ExchangeData) {
        this.selectedOffersCard = card;
    }

    selectedUserRatingRequested() {
        return this.isRequester(this.selectedRequestsCard) ? this.selectedRequestsCard?.offeredPub?.book?.owner?.ratingAverage : this.selectedRequestsCard?.requestedPub?.book?.owner?.ratingAverage;
    }

    selectedUserRatingOffered() {
        return this.isRequester(this.selectedOffersCard) ? this.selectedOffersCard?.offeredPub?.book?.owner?.ratingAverage : this.selectedOffersCard?.requestedPub?.book?.owner?.ratingAverage;
    }

    redirectToPublications() {
        this.router.navigate(['/publications']);
    }

    isRequester(selectedCard: ExchangeData | null) {
        return this.loggedUser?.username == selectedCard?.requestedPub.book?.owner?.username;
    }


    loadRequestVariablesNames() {
        this.Title = this.translate.instant('EXCHANGES.REQUESTS_TITLE');
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

    paginationOffered: Pagination | null = null;
    paginationSolicited: Pagination | null = null;

    currentOfferedPage: number = 0;
    currentSolicitedPage: number = 0;

    getOffered(url: string) {
        this.loadExchanges(url, true);
    }

    getSolicited(url: string) {
        this.loadExchanges(url, false);
    }
}