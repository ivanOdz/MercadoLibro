import {Component} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";

@Component({
    selector: 'exchanges-requests',
    templateUrl: `requests.component.html`,
    standalone: true,
    styleUrl: './exchanges.component.css',
    imports: [
        SidebarComponent
    ]
})
export class RequestsComponent {
    Title = "Requests";
}