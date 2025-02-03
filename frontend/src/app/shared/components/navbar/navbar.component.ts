import { Component } from '@angular/core';
import { MenubarModule } from 'primeng/menubar';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.css'],
    standalone: true,
    imports: [MenubarModule]
})
export class NavbarComponent {
    items = [
        { label: 'Exchanges', icon: 'pi pi-home', routerLink: '/exchanges' },
        { label: 'Profile', icon: 'pi pi-info', routerLink: '/profile' }
    ];
}