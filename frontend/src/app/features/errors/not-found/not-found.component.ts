import { Component } from '@angular/core';
import { ErrorComponent } from '../error/error.component';

@Component({
  selector: 'app-not-found',
  imports: [
    ErrorComponent
  ],
  templateUrl: './not-found.component.html',
  standalone: true,
  styleUrl: './not-found.component.css'
})
export class NotFoundComponent {

}
