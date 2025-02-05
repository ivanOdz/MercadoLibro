import {Component} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {Tab, TabList, TabPanel, TabPanels, Tabs} from "primeng/tabs";
import {NgForOf} from "@angular/common";

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
        NgForOf
    ]
})
export class RequestsComponent {
    Title = "Requests";

    selectedOffersCard: string | null = null;
    selectedRequestsCard: string | null = null;

    selectRequestCard(cardText: string) {
        this.selectedRequestsCard = cardText;
    }

    selectOfferCard(cardText: string) {
        this.selectedOffersCard = cardText;
    }
}