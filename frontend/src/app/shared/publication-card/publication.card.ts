import {Component, Input} from "@angular/core";
import { PublicationData} from "../../core/models/types";

@Component({
    selector: 'publication-card',
    templateUrl: './publication.card.html',
    styleUrl: './publication.card.css',
    standalone: true,
})
export class PublicationCardComponent {
    @Input() publication!: PublicationData;
}