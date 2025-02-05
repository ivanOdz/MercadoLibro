import {Component} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";

@Component({
    selector: 'exchanges-requests',
    templateUrl: `requests.component.html`,
    standalone: true,
    styleUrl: './exchanges.component.css',
    imports: [
        SidebarComponent,
        NavbarComponent
    ]
})
export class RequestsComponent {
    Title = "Requests";
}