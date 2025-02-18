import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {NavbarComponent} from "../../shared/navbar/navbar.component";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {Button} from "primeng/button";
import { Carousel } from 'primeng/carousel';
import { trigger, style, animate, transition } from '@angular/animations';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import {PrimeTemplate} from "primeng/api";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForOf, NgIf, Location as AngularLocation, AsyncPipe} from "@angular/common";
import {Select} from "primeng/select";
import {FormsModule} from "@angular/forms";
import {environment} from "../../../environments/environment";
import {Divider} from "primeng/divider";
import {Paginator} from "primeng/paginator";
import { BookData, PublicationData} from "../../core/models/types";
import {PublicationService} from "../../core/services/publication.service";
import {BehaviorSubject, catchError, filter, forkJoin, of, switchMap} from "rxjs";
import {map, tap} from "rxjs/operators";
import {UserService} from "../../core/services/user.service";
import {BookService} from "../../core/services/book.service";
import {BookModelService} from "../../core/services/bookmodel.service";
import {User} from "../../core/models/user.model";
import {Rating} from "primeng/rating";
import {AuthService} from "../../core/services/auth.service";
import {Tooltip} from "primeng/tooltip";
import {ExchangeService} from "../../core/services/exchange.service";
import {Location} from "../../core/models/location.model";
import {Dialog} from "primeng/dialog";

@Component({
    selector: 'app-publication-detail',
    templateUrl: `./publication.component.html`,
    standalone: true,
    imports: [
        NavbarComponent,
        TranslatePipe,
        Button,
        Carousel,
        PrimeTemplate,
        NgIf,
        ScrollPanelModule,
        Select,
        FormsModule,
        Divider,
        Paginator,
        NgForOf,
        Rating,
        Tooltip,
        Dialog,
        AsyncPipe
    ],
    animations: [
        trigger('fadeOutUp', [
            transition(':leave', [
                animate('500ms ease-in', style({ opacity: 0, transform: 'translateY(-20px)' }))
            ]),
            transition(':enter', [
                style({ opacity: 0, transform: 'translateY(20px)' }),
                animate('500ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
            ])
        ]),
        trigger('fadeOutIn', [
            transition(':leave', [
                animate('500ms ease-in', style({ opacity: 0, transform: 'translateY(20px)' }))
            ]),
            transition(':enter', [
                style({ opacity: 0, transform: 'translateY(-20px)' }),
                animate('500ms ease-out', style({ opacity: 1, transform: 'translateY(0)' }))
            ]),
        ])
    ],
    styleUrls: ['./publication.component.css']
})
export class PublicationComponent implements OnInit {

    publication: PublicationData | null = null;
    publicationLocations: string[] = [];
    bookImages: string[] = [];


    userBooks: BookData[] = [];
    userLocations$ = new BehaviorSubject<Location[]>([]);

    selectedLocation: Location | null = null;
    errorNoLocation: boolean = false;

    selectedBookIndex: number | null = null;
    errorNoBook: boolean = false;

    exchangeSolicited: boolean = false;

    fromBooks: boolean = false;
    modalEditLocationsVisible: boolean = false;
    selectedAddLocation: Location | null = null;
    errorNoAddLocation: boolean = false;

    userIsLogged: boolean = false;

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            const id = params['id'];
            this.loadPublication(id);

        });
        this.route.queryParams.subscribe(params => {
            this.fromBooks = params['origen'] !== 'publications';
        });
    }

    constructor(private translate: TranslateService,
                private router: Router,
                private route: ActivatedRoute,
                private ps: PublicationService,
                private us: UserService,
                private as: AuthService,
                private es: ExchangeService,
                private bs: BookService,
                private bms: BookModelService,
				private angularLocation: AngularLocation,
                private cdRef: ChangeDetectorRef) {
    }


    /** API **/

    loadPublication(id: string) {
        this.ps.getPublication(`${environment.production ? environment.productionUrl : environment.developmentUrl}/publications/${id}`).pipe(
            switchMap((publication) => {
                if (!publication) {
                    throw new Error('No se encontró la publicación');
                }
                return forkJoin({
                    publication: of(publication),
                    book: this.bs.getBook(publication.book),
                    user: this.us.getUser(publication.user),
                    locations: this.us.getLocationsInPublication(publication.locations)
                });
            }),
            switchMap(({ publication, book, user, locations }) => {
                if (!book) {
                    throw new Error('No se encontró el libro');
                }
                return forkJoin({
                    publication: of(publication),
                    book: of(book),
                    user: of(user),
                    locations: of(locations),
                    bookModel: this.bms.getBookModel(book.bookModel)
                });
            }),
            catchError((error) => {
                console.error(error);
                return of(null);
            })
        ).subscribe((data) => {
            if (data) {
                this.bookImages = data.book.images;
                this.publicationLocations = data.locations.map(location => location.location);
                this.publication = {
                    book: {
                        state: data.book.state,
                        available: data.book.available,
                        owner: data.user,
                        bookModel: data.bookModel,
                        images: data.book.images,
                        self: data.book.self
                    },
                    locations: data.locations,
                    user: data.user,
                    publicationState: data.publication.publicationState,
                    publicationDatetime: new Date(data.publication.publicationDatetime),
                    favoriteEndpoint: data.publication.favoriteEndpoint,
                    favoritePublication: null,
                    isFavoriteTemplate: '',
                    self: data.publication.self,
                    publication: data.publication
                };

                this.as.loggedUser$.pipe(
                    tap(user => {
                        if (user !== null && user.self === data.user?.self) {
                            this.loadUserLocations(false)
                        }
                        if (user !== null) {
                            this.userIsLogged = true;
                        }
                    })
                ).subscribe(
                    () => {}
                )
            }
        });
    }

    toggleSolicitExchange() {
        this.exchangeSolicited = !this.exchangeSolicited;

        if (this.exchangeSolicited) {
            this.loadBooksData()
            this.loadUserLocations(true)
        }
    }

    loadBooksData() {
        this.as.loggedUser$.pipe(
            filter(user => !!user),
            switchMap((user: User) => {
                return this.bs.getBooks({
                    booksUrl: user.books,
                    state: '',
                    genre: '',
                    search: '',
                    sort: '',
                    available: true
                });
            }),
            switchMap(({ books, pagination }) => {
                // TODO: manejar la paginación más adelante
                return forkJoin(
                    books.map(book =>
                        this.bms.getBookModel(book.bookModel).pipe(
                            map(bookModel => ({
                                state: book.state,
                                available: book.available,
                                owner: book.owner ?? null,
                                bookModel: bookModel,
                                images: book.images ?? [],
                                self: book.self ?? null
                            }) as unknown as BookData)
                        )
                    )
                );
            })
        ).subscribe({
            next: (response) => {
                this.userBooks = response;
            },
            error: (err) => {
                console.error("Error al obtener los libros:", err);
            }
        });
    }


    loadUserLocations(exchangeStarted: boolean) {
        this.as.loggedUser$.pipe(
            filter(user => !!user),
            switchMap((user: User) => {
                return this.us.getLocations(user);
            })
        ).subscribe({
            next: (locations) => {
                let updatedLocations = locations.map(location => ({
                    location: location.location,
                    publications: location.publications,
                    self: location.self
                }) as Location);

                if (!exchangeStarted) {
                    updatedLocations = updatedLocations.filter(
                        location => !this.publication?.locations?.some(
                            pubLocation => pubLocation.self === location.self
                        )
                    );
                    this.userLocations$.next(updatedLocations);
                    this.cdRef.detectChanges();
                }

            },
        });
    }

    requestExchange() {
        if (this.selectedLocation == null && this.selectedBookIndex == null) {
            this.errorNoBook = true;
            this.errorNoLocation = true;
            return;
        } else if (this.selectedLocation == null) {
            this.errorNoLocation = true;
            return;
        } else if (this.selectedBookIndex == null) {
            this.errorNoBook = true;
            return;
        }

        let exchangesUrn = `${environment.production ? environment.productionUrl : environment.developmentUrl}/exchanges`

        this.es.createExchange(exchangesUrn, this.userBooks[this.selectedBookIndex].self, this.selectedLocation.self, this.publication?.self).subscribe({
            next: () => {
                this.router.navigate(['/exchanges/requests'], { queryParams: { selectedTab: 1 } });
            }
        });
    }


    /** HTML **/

    selectBook(index: number) {
        this.selectedBookIndex = this.selectedBookIndex === index ? null : index;
    }

    goToPublications() {
        this.router.navigate(['/publications']);
    }

    get userRating(): number {
        return this.publication?.user?.ratingAverage ?? 0;
    }

    set userRating(value: number) {
        if (this.publication?.user) {
            this.publication.user.ratingAverage = value;
        }
    }

    getImages(bookImages: string[] | null = null): string[] {
        return bookImages || [this.getDefaultImage()];
    }

    getDefaultImage() {
        return `${environment.production? 'http://pawserver.it.itba.edu.ar/paw-2024b-09'  : 'http://localhost:8080'}/assets/book.jpg`;
    }

    getCover(book: BookData | null = null) {
        console.log("publication imge: ", book?.bookModel?.cover);
        return book?.bookModel?.cover ||
            this.getImages(book?.images)[0] || this.getDefaultImage();
    }

    getFormattedDate(time: Date | null | undefined) {
        if (!time) {
            return "";
        }
        const date = new Date(time);
        switch (this.translate.currentLang) {
            case 'es':
                return date.toLocaleDateString('es-ES', { day: 'numeric', month: 'long' });
            case 'en':
                return date.toLocaleDateString('en-GB', { day: 'numeric', month: 'long' }); // en-GB para evitar coma en inglés
        }
        return "";
    }

    getConditionTranslation(state: string | undefined) {
        switch (state) {
            case 'NEW':
                return this.translate.instant('bookstate.' + state.toLowerCase());
            case 'LIKE_NEW':
                return this.translate.instant('bookstate.like.new');
            case 'VERY_GOOD':
                return this.translate.instant('bookstate.very.good');
            case 'GOOD':
                return this.translate.instant('bookstate.' + state.toLowerCase());
            case 'ACCEPTABLE':
                return this.translate.instant('bookstate.' + state.toLowerCase());
            case 'WORN':
                return this.translate.instant('bookstate.' + state.toLowerCase());
            default:
                return "";
        }
    }

    redirectToBookModels() {
//        this.router.navigate(['/book_models'])
    }

    redirectToProfile() {
        this.router.navigate(['/profile'])
    }

    isUserPublication(): boolean {
        let isUserPublication = false;
        this.as.loggedUser$.subscribe(user => {
            isUserPublication = this.publication?.user?.self === user?.self;
        });
        return isUserPublication;
    }


    goToMyBooks() {
        this.router.navigate(['/my-books']);
    }
	
	goBack() {
		if (window.history.length > 2) {
			this.angularLocation.back();
		}
		else {
			this.router.navigate(['/']);
		}
	}

    toggleEditModal() {
        this.modalEditLocationsVisible = !this.modalEditLocationsVisible;
    }

    editPublicationLocations() {
        if (!this.selectedAddLocation) {
            this.errorNoAddLocation = true;
            return;
        }

        this.as.loggedUser$.pipe(
            filter(user => !!user),
            switchMap((user: User) =>
                this.ps.addLocation(this.publication?.self, this.selectedAddLocation?.self)
            )
        ).subscribe({
            next: () => {

                if (this.publication) {
                    this.us.getLocationsInPublication(this.publication.publication?.locations).subscribe(
                        locations => {
                            this.publicationLocations = locations.map(location => location.location);
                        }
                    );
                    window.location.reload()

                }
            },

            error: (err) => {
                console.error("Error adding location:", err);
            }
        });

    }


}