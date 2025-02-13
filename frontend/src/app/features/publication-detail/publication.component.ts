import {Component, OnInit} from "@angular/core";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {Button} from "primeng/button";
import { Carousel } from 'primeng/carousel';
import { trigger, style, animate, transition } from '@angular/animations';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import {PrimeTemplate} from "primeng/api";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForOf, NgIf} from "@angular/common";
import {Select} from "primeng/select";
import {FormsModule} from "@angular/forms";
import {environment} from "../../../environments/environment";
import {Divider} from "primeng/divider";
import {Paginator} from "primeng/paginator";
import { BookData2, PublicationData2} from "../../core/models/types";
import {PublicationService} from "../../core/services/publication.service";
import {catchError, filter, forkJoin, of, switchMap, tap} from "rxjs";
import {map} from "rxjs/operators";
import {UserService} from "../../core/services/user.service";
import {BookService} from "../../core/services/book.service";
import {BookModelService} from "../../core/services/bookmodel.service";
import {User} from "../../core/models/user.model";
import {Rating} from "primeng/rating";
import {AuthService} from "../../core/services/auth.service";
import {Tooltip} from "primeng/tooltip";
import {ExchangeService} from "../../core/services/exchange.service";
import {Location} from "../../core/models/location.model";
import {routes} from "../../app.routes";

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
        Tooltip
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

    publication: PublicationData2 | null = null;
    publicationLocations: string[] = [];
    bookImages: string[] = [];
    selectedBookIndex: number | null = null;


    userBooks: BookData2[] = [];
    userLocations: Location[] = [];
    selectedLocation: Location | null = null;
    exchangeSolicited: boolean = false;

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            const id = params['id'];
            this.loadPublication(id);
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
                private bms: BookModelService) {
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
                    self: data.publication.self
                };
            }
        });
    }

    toggleSolicitExchange() {
        this.exchangeSolicited = !this.exchangeSolicited;

        if (this.exchangeSolicited) {
            this.loadBooksData()
            this.loadUserLocations()
        }
    }

    loadBooksData() {
        this.as.loggedUser$.pipe(
            filter(user => !!user),
            switchMap((user: User) => {
                return this.bs.getBooks({
                    booksUrl: user.books,
                    state: 'AVAILABLE',
                    genre: '',
                    search: ''
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
                            }) as unknown as BookData2)
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


    loadUserLocations() {
        this.as.loggedUser$.pipe(
            filter(user => !!user),
            switchMap((user: User) => {
                return this.us.getLocations(user);
            })
        ).subscribe({
            next: (locations) => {
                this.userLocations = locations.map(location => {
                    return {
                        location: location.location,
                        publications: location.publications,
                        self: location.self
                    } as Location;
                });
            },
        });
    }

    requestExchange() {
        if (this.selectedLocation == null) {
            // error management
            return;
        }
        if (!this.selectedBookIndex) {
            // error management
            return;
        }

        let exchangesUrn = `${environment.production ? environment.productionUrl : environment.developmentUrl}/exchanges`

        this.es.createExchange(exchangesUrn, this.publication?.book?.self, this.selectedLocation.self, this.publication?.self).subscribe({
            next: (response) => {
                this.router.navigate(['/exchanges/requests']);
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

    getImages(){
        return this.bookImages.length === 0 ? [this.getDefaultImage()] : this.bookImages;
    }

    getDefaultImage() {
        return `${environment.production? 'http://pawserver.it.itba.edu.ar/paw-2024b-09'  : 'http://localhost:8080'}/assets/book.jpg`;
    }

    getCover() {
        return this.publication?.book?.bookModel?.coverUri || this.getDefaultImage();
    }
}