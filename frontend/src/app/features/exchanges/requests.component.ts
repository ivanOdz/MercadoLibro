import {Component} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {Tab, TabList, TabPanel, TabPanels, Tabs} from "primeng/tabs";
import {NgForOf} from "@angular/common";
import {Paginator, PaginatorState} from "primeng/paginator";
import {Rating} from "primeng/rating";
import {FormsModule} from "@angular/forms";
import {Button} from "primeng/button";

@Component({
    selector: 'exchanges-requests',
    templateUrl: `requests.component.html`,
    standalone: true,
    styleUrl: './exchanges.component.css',
    imports: [
        SidebarComponent,
        NavbarComponent,
        Tabs,
        TabList,
        Tab,
        TabPanels,
        TabPanel,
        NgForOf,
        Paginator,
        Rating,
        FormsModule,
        Button
    ]
})
export class RequestsComponent {
    Title = "Requests";

    selectedOffersCard: string | null = null;
    selectedRequestsCard: string | null = null;
    first: number;
    rows: unknown;
    totalRecords: unknown;
    value: any;
    
    constructor() {
        this.first = 1;
    }

    selectRequestCard(cardText: string) {
        this.selectedRequestsCard = cardText;
    }

    selectOfferCard(cardText: string) {
        this.selectedOffersCard = cardText;
    }

    onPageChange($event: PaginatorState) {
        
    }
}