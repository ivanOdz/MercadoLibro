import {Component, OnInit} from "@angular/core";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {Button} from "primeng/button";
import { Carousel } from 'primeng/carousel';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import {PrimeTemplate} from "primeng/api";
import {routes} from "../../app.routes";
import {ActivatedRoute, Router} from "@angular/router";
import {NgForOf, NgIf} from "@angular/common";
import {Select} from "primeng/select";
import {FormsModule} from "@angular/forms";
import {environment} from "../../../environments/environment";
import {Divider} from "primeng/divider";
import {Paginator} from "primeng/paginator";
import {PublicationData2} from "../../core/models/types";
import {PublicationService} from "../../core/services/publication.service";
import {catchError, forkJoin, of, switchMap, tap} from "rxjs";
import {map} from "rxjs/operators";
import {UserService} from "../../core/services/user.service";
import {BookService} from "../../core/services/book.service";
import {BookModelService} from "../../core/services/bookmodel.service";
import {User} from "../../core/models/user.model";
import {BookModel} from "../../core/models/bookModel.model";
import {Rating} from "primeng/rating";

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
        Rating
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
    bookImages: string[] = [];
    publicationLocations: string[] = [];

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            const id = params['id'];
            this.loadPublication(id);
        });
    }

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


    constructor(private translate: TranslateService,
                private router: Router,
                private route: ActivatedRoute,
                private ps: PublicationService,
                private us: UserService,
                private bs: BookService,
                private bms: BookModelService) {
        this.translate.onLangChange.subscribe(() => this.loadPublicationDetailItems());
    }

    loadPublicationDetailItems() {
        // static data translations
    }

    books = [
        { title: "Libro 1" }, { title: "Libro 2" }, { title: "Libro 3" },
        { title: "Libro 4" }, { title: "Libro 5" }, { title: "Libro 6" },
        { title: "Libro 7" }, { title: "Libro 8" }, { title: "Libro 9" },
        { title: "Libro 10" }, { title: "Libro 11" }, { title: "Libro 12" },
        { title: "Libro 13" }, { title: "Libro 14" }
    ];

    selectedBookIndex: number | null = null;

    selectBook(index: number) {
        this.selectedBookIndex = this.selectedBookIndex === index ? null : index;
    }


    protected readonly routes = routes;
    protected readonly Router = Router;

    goToPublications() {
        this.router.navigate(['/publications']);
    }

    exchangeSolicited: boolean = false;

    toggleSolicitExchange() {
        this.exchangeSolicited = !this.exchangeSolicited;
    }

    userLocations: string[] = ['São Paulo', 'Rio de Janeiro', 'Minas Gerais', 'Bahia', 'Paraná', 'Santa Catarina', 'Rio Grande do Sul', 'Pernambuco', 'Ceará', 'Pará', 'Maranhão', 'Goiás', 'Distrito Federal', 'Espírito Santo', 'Mato Grosso', 'Mato Grosso do Sul', 'Paraíba', 'Rio Grande do Norte', 'Alagoas', 'Sergipe', 'Tocantins', 'Rondônia', 'Acre', 'Amapá', 'Roraima'];
    selectedLocation: string = '';

    requestExchange() {

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