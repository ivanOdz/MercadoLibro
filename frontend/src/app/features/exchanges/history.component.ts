import {Component} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";

@Component({
    selector: 'exchanges-history',
    templateUrl: `history.component.html`,
    standalone: true,
    styleUrl: './exchanges.component.css',
    imports: [
        SidebarComponent
    ]
})
export class HistoryComponent {
    Title = "History";
}