import { Component } from '@angular/core';
import {ErrorComponent} from "../error/error.component";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-unauthorized',
  imports: [
    ErrorComponent,
    TranslatePipe
  ],
  templateUrl: './unauthorized.component.html',
  styleUrl: './unauthorized.component.css'
})
export class UnauthorizedComponent {

}
