import { Component, ViewChild } from '@angular/core';
import { MenubarModule } from 'primeng/menubar';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { ButtonModule } from 'primeng/button';
import { MenuItem } from 'primeng/api';
import { LanguageSwitcherComponent } from '../language-switcher/language-switcher.component';
import { TieredMenu } from 'primeng/tieredmenu';
import {Router, RouterLink} from "@angular/router";
import { TranslatePipe, TranslateService } from "@ngx-translate/core";
import { AuthService } from "../../core/services/auth.service";
import { NgOptimizedImage } from "@angular/common";
import {FormsModule} from "@angular/forms";

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css'],
    standalone: true,
    imports: [
        MenubarModule,
        TieredMenuModule,
        ButtonModule,
        LanguageSwitcherComponent,
        RouterLink,
        TranslatePipe,
        NgOptimizedImage,
        FormsModule
    ]
})
export class NavbarComponent {
    searchQuery: string = '';

    @ViewChild('profileMenu') profileMenu!: TieredMenu;
    profileItems: MenuItem[] = [];

    constructor(private authService: AuthService, private translate: TranslateService, private router: Router) {
        this.translate.onLangChange.subscribe(() => this.loadProfileItems());
        this.loadProfileItems();
    }

    loadProfileItems() {
        this.profileItems = [
            { label: this.translate.instant('NAVBAR.PROFILE'), routerLink: '/profile' },
            { label: this.translate.instant('NAVBAR.MY_BOOKS'), routerLink: '/my-books' },
            { label: this.translate.instant('NAVBAR.MY_PUBLICATIONS'), routerLink: '/publications/mine' },
            { label: this.translate.instant('NAVBAR.FAVORITES'), routerLink: '/publications/favorites' },
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
		window.location.reload();
    }

    search() {
        if (this.searchQuery) {
            this.router.navigate(['/publications'],
                { queryParams: { search: this.searchQuery },
                    queryParamsHandling: 'merge' });
        }
    }

}