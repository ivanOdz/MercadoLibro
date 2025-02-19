import {Component, OnInit, ViewChild} from '@angular/core';
import { MenubarModule } from 'primeng/menubar';
import { ButtonModule } from 'primeng/button';
import { MenuItem } from 'primeng/api';
import { LanguageSwitcherComponent } from '../language-switcher/language-switcher.component';
import {Router, RouterLink} from "@angular/router";
import { TranslatePipe, TranslateService } from "@ngx-translate/core";
import { AuthService } from "../../core/services/auth.service";
import {NgIf, NgOptimizedImage} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {TieredMenu, TieredMenuModule} from "primeng/tieredmenu";

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
        FormsModule,
        NgIf
    ]
})
export class NavbarComponent implements OnInit{
    searchQuery: string = '';

    @ViewChild('profileMenu') profileMenu!: TieredMenu;
    profileItems: MenuItem[] = [];

    isLoggedIn: boolean = false;

    ngOnInit(): void {
        this.authService.loggedUser$.subscribe(user => {
            if (user) {
                this.loadProfileItems({isLogged: true});
                this.isLoggedIn = true;
            }
            else {
                this.isLoggedIn = false;
                this.loadProfileItems({isLogged: false});
            }
        });
    }

    constructor(private authService: AuthService, private translate: TranslateService, private router: Router) {
    }

    loadProfileItems({isLogged}: { isLogged: boolean }) {
        if(!isLogged){
            return
        }

        this.profileItems = [
            { label: this.translate.instant('NAVBAR.PROFILE'), routerLink: '/auth/profile' },
            { label: this.translate.instant('NAVBAR.MY_BOOKS'), routerLink: '/books' },
            { label: this.translate.instant('NAVBAR.MY_PUBLICATIONS'), routerLink: '/publications/mine' },
            { label: this.translate.instant('NAVBAR.FAVORITES'), routerLink: '/publications/favorites' },
            { separator: true },
            {
                label: this.translate.instant('NAVBAR.LOGOUT'),
                icon: 'pi pi-sign-out',
                id: 'logout-item',
                command: () => this.logout()
            }
        ];
    }

    toggleProfileMenu(event: MouseEvent) {
        this.profileMenu.toggle(event);
    }

    logout() {
        this.authService.logout();
        this.router.navigateByUrl('/publications');

    }
}