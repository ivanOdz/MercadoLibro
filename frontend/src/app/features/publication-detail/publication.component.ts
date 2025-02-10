import {Component, OnInit} from "@angular/core";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {Button} from "primeng/button";
import { Carousel } from 'primeng/carousel';
import {NgOptimizedImage} from "@angular/common";
import {PrimeTemplate} from "primeng/api";
import {routes} from "../../app.routes";
import {Router} from "@angular/router";

@Component({
    selector: 'app-publication-detail',
    templateUrl: `./publication.component.html`,
    standalone: true,
    imports: [
        NavbarComponent,
        TranslatePipe,
        Button,
        Carousel,
        PrimeTemplate
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
        'http://localhost:8080/assets/book.jpg',
        'http://localhost:8080/assets/book.jpg',
        'http://localhost:8080/assets/book.jpg',
        'http://localhost:8080/assets/book.jpg',
        'http://localhost:8080/assets/book.jpg',
        'http://localhost:8080/assets/book.jpg',
        'http://localhost:8080/assets/book.jpg'
    ];

    protected readonly routes = routes;
    protected readonly Router = Router;

    goToPublications() {
        this.router.navigate(['/publications']);
    }
}