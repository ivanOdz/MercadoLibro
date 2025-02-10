import {Component, OnInit} from "@angular/core";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";

@Component({
    selector: 'app-publication-detail',
    templateUrl: `./publication.component.html`,
    standalone: true,
    imports: [
        NavbarComponent
    ],
    styleUrls: ['./publication.component.css']
})
export class PublicationComponent implements OnInit {

    ngOnInit(): void {
    }

}