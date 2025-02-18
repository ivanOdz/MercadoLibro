import { Component, Input } from '@angular/core';
import {RouterLink} from '@angular/router';
import { NavbarComponent } from '../../../shared/navbar/navbar.component';


@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  imports: [
    NavbarComponent,
    RouterLink,
  ],
  styleUrls: ['./error.component.css'],
  standalone: true
})
export class ErrorComponent {
  @Input() title: string = 'Error';
  @Input() message: string = 'Ha ocurrido un problema.';
  @Input() redirectText: string = 'Si deseas, puedes ';
  @Input() buttonText: string = 'Volver al inicio';
  @Input() redirectUrl: string = '/';
  @Input() imageSrc: string = 'assets/default-error.png';
}
