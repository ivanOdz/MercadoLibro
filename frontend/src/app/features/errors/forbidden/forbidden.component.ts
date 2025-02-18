import { Component } from '@angular/core';
import {ErrorComponent} from "../error/error.component";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-forbidden',
  imports: [
    ErrorComponent,
    TranslatePipe
  ],
  templateUrl: './forbidden.component.html',
  styleUrl: './forbidden.component.css'
})
export class ForbiddenComponent {

}
