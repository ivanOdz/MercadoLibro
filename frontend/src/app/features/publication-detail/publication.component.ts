import {Component, OnInit} from "@angular/core";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {Button} from "primeng/button";
import { Carousel } from 'primeng/carousel';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import {PrimeTemplate} from "primeng/api";
import {routes} from "../../app.routes";
import {Router} from "@angular/router";
import {NgForOf, NgIf} from "@angular/common";
import {Select} from "primeng/select";
import {FormsModule} from "@angular/forms";
import {environment} from "../../../environments/environment";
import {Divider} from "primeng/divider";
import {Paginator} from "primeng/paginator";

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
        NgForOf
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

    ngOnInit(): void {
    }

    constructor(private translate: TranslateService, private router: Router) {
        this.translate.onLangChange.subscribe(() => this.loadPublicationDetailItems());
    }

    loadPublicationDetailItems() {
        // static data translations
    }


    bookImages: string[] = [
        `${environment.production ? environment.productionUrl : environment.developmentUrl}/assets/book.jpg`,
        `${environment.production ? environment.productionUrl : environment.developmentUrl}/assets/book.jpg`,
        `${environment.production ? environment.productionUrl : environment.developmentUrl}/assets/book.jpg`,
        `${environment.production ? environment.productionUrl : environment.developmentUrl}/assets/book.jpg`,
        `${environment.production ? environment.productionUrl : environment.developmentUrl}/assets/book.jpg`,
        `${environment.production ? environment.productionUrl : environment.developmentUrl}/assets/book.jpg`,
        `${environment.production ? environment.productionUrl : environment.developmentUrl}/assets/book.jpg`,
    ];

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
}