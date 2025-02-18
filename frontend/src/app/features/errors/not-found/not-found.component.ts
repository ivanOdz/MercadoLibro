import { Component } from '@angular/core';
import { ErrorComponent } from '../error/error.component';
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-not-found',
    imports: [
        ErrorComponent,
        TranslatePipe
    ],
  templateUrl: './not-found.component.html',
  standalone: true,
  styleUrl: './not-found.component.css'
})
export class NotFoundComponent {

}
