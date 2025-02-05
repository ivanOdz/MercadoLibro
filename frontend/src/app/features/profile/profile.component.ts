import {Component, inject} from '@angular/core';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import {NgForOf, NgOptimizedImage} from '@angular/common';
import { User } from '../../core/models/user.model';
import { UserService } from '../../core/services/user.service';

@Component({
  selector: 'app-profile',
  imports: [NavbarComponent, NgOptimizedImage, NgForOf],
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  //user: User;
  userServices: UserService = inject(UserService);

  constructor() {
    //this.user = this.userServices.getUser();
  }

}

