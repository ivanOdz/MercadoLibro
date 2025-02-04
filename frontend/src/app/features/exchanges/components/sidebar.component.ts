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
        {label: 'Active', routerLink: '/exchanges'},
        {label: 'Requests', routerLink: '/exchanges/requests'},
        {label: 'History', routerLink: '/exchanges/history'},
    ]
}