import {Component} from "@angular/core";
import {SidebarComponent} from "./components/sidebar.component";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import {NgForOf} from "@angular/common";
import {Tab, TabList, TabPanel, TabPanels, Tabs} from "primeng/tabs";
import {Paginator} from "primeng/paginator";
import {Rating} from "primeng/rating";
import {FormsModule} from "@angular/forms";
import {Button} from "primeng/button";
import {Textarea} from "primeng/textarea";
import {Popover} from "primeng/popover";
import {StyleClass} from "primeng/styleclass";

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
        Tabs,
        Paginator,
        Rating,
        FormsModule,
        Button,
        Textarea,
        Popover,
        StyleClass
    ]
})
export class HistoryComponent {
    Title = "History";


    selectedCompletedCard: string | null = null;
    selectedRejectedCard: string | null = null;
    first: number;
    rows: unknown;
    totalRecords: unknown;
    value: any = 5;
    showContent = false;

    reviewText: string = '';
    reviewValue: number = 0;

    toggleReviewContent() {
        this.showContent = !this.showContent;
    }

    selectCompletedCard(cardText: string) {
        this.selectedCompletedCard = cardText;
    }

    selectRejectedCard(cardText: string) {
        this.selectedRejectedCard = cardText;
    }
    
    onPageChange($event: any) {
        console.log($event);
    }
    
    constructor() {
        this.first = 1;
    }

    confirmReview() {

    }
}