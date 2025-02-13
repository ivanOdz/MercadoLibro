import {Component, OnInit} from "@angular/core";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgForOf} from "@angular/common";
import {Button} from "primeng/button";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";

@Component({
    selector: 'exchanges-sidebar',
    templateUrl: `sidebar.component.html`,
    standalone: true,
    styleUrl: '../exchanges.component.css',
    imports: [
        RouterLink,
        RouterLinkActive,
        NgForOf,
        Button,
        TranslatePipe
    ]
})
export class SidebarComponent implements OnInit {
    items: any[] = []

    constructor(private translate: TranslateService) {
    }

    ngOnInit(): void {
        this.translate.onLangChange.subscribe(() => this.loadItems());
        this.loadItems();
    }

    loadItems() {
        this.items = [
            {label:  this.translate.instant('EXCHANGES.ACTIVE_SIDEBAR'), routerLink: '/exchanges', icon: 'pi pi-arrow-right-arrow-left'},
            {label:  this.translate.instant('EXCHANGES.REQUESTS_SIDEBAR'), routerLink: '/exchanges/requests', icon: 'pi pi-bell'},
            {label: this.translate.instant('EXCHANGES.HISTORY_SIDEBAR'), routerLink: '/exchanges/history', icon: 'pi pi-history'},
        ]
    }
}