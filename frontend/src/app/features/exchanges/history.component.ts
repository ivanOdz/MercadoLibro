import {Component} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {NgForOf} from "@angular/common";
import {Tab, TabList, TabPanel, TabPanels, Tabs} from "primeng/tabs";

@Component({
    selector: 'exchanges-history',
    templateUrl: `history.component.html`,
    standalone: true,
    styleUrl: './exchanges.component.css',
    imports: [
        SidebarComponent,
        NavbarComponent,
        NgForOf,
        Tab,
        TabList,
        TabPanel,
        TabPanels,
        Tabs
    ]
})
export class HistoryComponent {
    Title = "History";


    selectedCompletedCard: string | null = null;
    selectedRejectedCard: string | null = null;

    selectCompletedCard(cardText: string) {
        this.selectedCompletedCard = cardText;
    }

    selectRejectedCard(cardText: string) {
        this.selectedRejectedCard = cardText;
    }
}