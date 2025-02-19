import { Component } from '@angular/core';
import {ErrorComponent} from "../error/error.component";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-internal-server-error',
  imports: [
    ErrorComponent,
    TranslatePipe
  ],
  templateUrl: './internal-server-error.component.html',
  styleUrl: './internal-server-error.component.css'
})
export class InternalServerErrorComponent {

}