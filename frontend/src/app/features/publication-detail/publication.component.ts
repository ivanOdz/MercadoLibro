import {Component, OnInit} from "@angular/core";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {Button} from "primeng/button";

@Component({
    selector: 'app-publication-detail',
    templateUrl: `./publication.component.html`,
    standalone: true,
    imports: [
        NavbarComponent,
        TranslatePipe,
        Button
    ],
    styleUrls: ['./publication.component.css']
})
export class PublicationComponent implements OnInit {

    ngOnInit(): void {
    }

    constructor(private translate: TranslateService) {
        this.translate.onLangChange.subscribe(() => this.loadPublicationDetailItems());
    }

    loadPublicationDetailItems() {
        // static data translations
    }

}