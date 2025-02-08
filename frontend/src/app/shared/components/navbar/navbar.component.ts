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
import { RouterLink } from "@angular/router";
import { TranslatePipe, TranslateService } from "@ngx-translate/core";
import { AuthService } from "../../../core/services/auth.service";
import { NgOptimizedImage } from "@angular/common";

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

    @ViewChild('profileMenu') profileMenu!: TieredMenu;
    profileItems: MenuItem[] = [];

    constructor(private authService: AuthService, private translate: TranslateService) {
        this.translate.onLangChange.subscribe(() => this.loadProfileItems());
        this.loadProfileItems();
    }

    loadProfileItems() {
        this.profileItems = [
            { label: this.translate.instant('NAVBAR.PROFILE'), routerLink: '/profile' },
            { label: this.translate.instant('NAVBAR.MY_BOOKS'), routerLink: '/my-books' },
            { label: this.translate.instant('NAVBAR.MY_PUBLICATIONS'), routerLink: '/my-publications' },
            { label: this.translate.instant('NAVBAR.FAVORITES'), routerLink: '/favorites' },
            { separator: true },
            {
                label: this.translate.instant('NAVBAR.LOGOUT'),
                icon: 'pi pi-sign-out',
                command: () => this.logout()
            }
        ];
    }

    toggleProfileMenu(event: Event) {
        this.profileMenu.toggle(event);
    }

    logout() {
        this.authService.logout();
    }
}