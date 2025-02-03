import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-publications',
  imports: [RouterModule],
  template: `
    <p>
      publications works!
    </p>
    <a [routerLink]="['/profile']">Go to your profile</a>
    <a [routerLink]="['/exchanges']">Check out your exchanges bro!</a>
  `,
  standalone: true,
  styleUrl: './publications.component.css'
})
export class PublicationsComponent {

}
