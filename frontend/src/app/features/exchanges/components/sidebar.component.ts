import {Component} from "@angular/core";
import {RouterLink, RouterLinkActive} from "@angular/router";
import {NgForOf} from "@angular/common";
import {Button} from "primeng/button";

@Component({
    selector: 'exchanges-sidebar',
    templateUrl: `sidebar.component.html`,
    standalone: true,
    styleUrl: '../exchanges.component.css',
    imports: [
        RouterLink,
        RouterLinkActive,
        NgForOf,
        Button
    ]
})
export class SidebarComponent {
    items = [
        {label: 'Current', routerLink: '/exchanges', icon: 'pi pi-arrow-right-arrow-left'},
        {label: 'Requests', routerLink: '/exchanges/requests', icon: 'pi pi-bell'},
        {label: 'History', routerLink: '/exchanges/history', icon: 'pi pi-history'},
    ]
}