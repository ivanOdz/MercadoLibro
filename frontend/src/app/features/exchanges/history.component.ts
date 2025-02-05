import {Component} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";

@Component({
    selector: 'exchanges-history',
    templateUrl: `history.component.html`,
    standalone: true,
    styleUrl: './exchanges.component.css',
    imports: [
        SidebarComponent,
        NavbarComponent
    ]
})
export class HistoryComponent {
    Title = "History";
}