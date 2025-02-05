import {Component, input} from '@angular/core';
import { MenubarModule } from 'primeng/menubar';
import {NgIf, NgOptimizedImage} from "@angular/common";
import {Ripple} from "primeng/ripple";
import {Badge} from "primeng/badge";
import {InputGroup} from "primeng/inputgroup";
import {InputGroupAddon} from "primeng/inputgroupaddon";
import {Button} from "primeng/button";
import {InputText} from "primeng/inputtext";

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css'],
    standalone: true,
    imports: [MenubarModule, InputGroup, InputGroupAddon, Button, InputText, NgOptimizedImage]
})
export class NavbarComponent {
    items = [
        { label: 'Exchanges', icon: 'pi pi-home', routerLink: '/exchanges' },
        { label: 'Profile', icon: 'pi pi-info', routerLink: '/profile' },
        { label: 'Publications', routerLink: '/publications' }
    ];
    protected readonly input = input;
}