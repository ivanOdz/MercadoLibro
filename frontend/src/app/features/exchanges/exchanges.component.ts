import { Component } from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";
import { ButtonModule } from 'primeng/button';
import {Title} from "@angular/platform-browser";


@Component({
  selector: 'app-exchanges',
  templateUrl: `exchanges.component.html`,
  standalone: true,
  styleUrl: './exchanges.component.css',
  imports: [ButtonModule]
})
export class ExchangesComponent {

  protected readonly Title = "Exchanges";
}
