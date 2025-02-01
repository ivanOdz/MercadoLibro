import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-publications',
  imports: [RouterModule],
  template: `
    <p>
      publications works!
    </p>
    <a [routerLink]="['/profile']">Profile</a>
  `,
  styleUrl: './publications.component.css'
})
export class PublicationsComponent {

}
