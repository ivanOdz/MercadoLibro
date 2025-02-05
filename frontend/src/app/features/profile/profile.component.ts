import { Component } from '@angular/core';
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";

@Component({
  selector: 'app-profile',
  imports: [
    NavbarComponent
  ],
  standalone: true,
  templateUrl: `./profile.component.html`,
  styleUrl: './profile.component.css'
})
export class ProfileComponent {

}