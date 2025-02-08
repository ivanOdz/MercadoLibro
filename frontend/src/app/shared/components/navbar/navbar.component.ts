import { Component, ViewChild } from '@angular/core';
import { MenubarModule } from 'primeng/menubar';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { ButtonModule } from 'primeng/button';
import { InputGroup } from 'primeng/inputgroup';
import { InputGroupAddon } from 'primeng/inputgroupaddon';
import { InputText } from 'primeng/inputtext';
import { MenuItem } from 'primeng/api';
import { LanguageSwitcherComponent } from '../language-switcher/language-switcher.component';
import { TieredMenu } from 'primeng/tieredmenu';
import {RouterLink} from "@angular/router";
import {TranslatePipe} from "@ngx-translate/core";
import {AuthService} from "../../../core/services/auth.service";
import {NgOptimizedImage} from "@angular/common";

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css'],
    standalone: true,
    imports: [
        MenubarModule,
        TieredMenuModule,
        ButtonModule,
        InputGroup,
        InputGroupAddon,
        InputText,
        LanguageSwitcherComponent,
        RouterLink,
        TranslatePipe,
        NgOptimizedImage
    ]
})
export class NavbarComponent {

    constructor(private authService: AuthService) {}
    @ViewChild('profileMenu') profileMenu!: TieredMenu;

    profileItems: MenuItem[] = [
        { label: 'Profile',  routerLink: '/profile' },
        { label: 'My Books',  routerLink: '/my-books' },
        { label: 'My Publications',  routerLink: '/my-publications' },
        { label: 'Favorites',  routerLink: '/favorites' },
        { separator: true },
        { label: 'Logout', icon: 'pi pi-sign-out', command: () => this.logout() }
    ];

    toggleProfileMenu(event: Event) {
        this.profileMenu.toggle(event);
    }

    logout() {
        this.authService.logout();
    }
}
