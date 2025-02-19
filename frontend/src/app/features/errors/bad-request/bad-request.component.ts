import { Component } from '@angular/core';
import {ErrorComponent} from "../error/error.component";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-bad-request',
  imports: [
    ErrorComponent,
    TranslatePipe
  ],
  templateUrl: './bad-request.component.html',
  styleUrl: './bad-request.component.css'
})
export class BadRequestComponent {

}
