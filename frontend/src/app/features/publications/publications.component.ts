import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";

@Component({
  selector: 'app-publications',
  imports: [RouterModule, NavbarComponent],
  templateUrl: `./publications.component.html`,
  standalone: true,
  styleUrl: './publications.component.css'
})
export class PublicationsComponent {

}
