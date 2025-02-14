import {Component, Input} from "@angular/core";
import { PublicationData} from "../../core/models/types";
import { Router} from "@angular/router";

@Component({
    selector: 'publication-card',
    templateUrl: './publication.card.html',
    styleUrl: './publication.card.css',
    standalone: true,
})
export class PublicationCardComponent {
    @Input() publication!: PublicationData;

    constructor(private router: Router) {
    }

    getBookImage(images: string[] | null | undefined) {
        return images? images[0] : 'assets/book.jpg';
    }


    goToPublicationDetail() {
        if (this.publication.self) {
            const path = this.publication.self.replace(/^.*\/api/, '');
            this.router.navigate([path]);
        }
    }

}