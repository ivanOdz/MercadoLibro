import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { CommonModule, NgForOf } from '@angular/common';
import { User } from '../../core/models/user.model';
import { UserService } from '../../core/services/user.service';
import { HttpClient } from "@angular/common/http";
import { AuthService } from '../../core/services/auth.service';
import { FormsModule } from '@angular/forms';
import { Location } from '../../core/models/location.model';
import { ReviewComponent } from './review/review.component';

@Component({
  selector: 'app-profile',
  imports: [NavbarComponent, NgForOf, FormsModule, CommonModule, ReviewComponent],
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  url = 'http://localhost:8080/api'
  userService: UserService = inject(UserService);

  loggedUser: User | null = null;
  newUsername = '';
  locations: Location[] = [];

  isModalOpen = false;


  constructor(private authService: AuthService, private http: HttpClient) {}

  openModal() {
    if (this.loggedUser) {
      this.newUsername = this.loggedUser.username;
    }
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

  updateUsername() {
    if (this.newUsername.trim()) {
      if(this.loggedUser) {
        this.userService.updateUsername(this.loggedUser, this.newUsername);
        console.log("Usuario cambiado a: ", this.newUsername);
     }
    }
    this.closeModal();
  }

  updateLanguage(event: Event) {
    const language = (event.target as HTMLSelectElement).value;
    if (this.loggedUser) {
      this.userService.updateLanguage(this.loggedUser, language);
      console.log("Idioma cambiado a: ", language);
    }
  }

  getLocations() {
    if (this.loggedUser) {
      this.userService.getLocations(this.loggedUser).subscribe(locations => {
        this.locations = locations;
      });
    } else {
      console.log("el usuario no está definido, no se pueden cargar las ubicaciones");
    }
  }

  addLocation(location: string) {
    if (this.loggedUser && this.loggedUser.locations.length < 5) {
      this.userService.addLocation(this.loggedUser, location);
    }
  }

  removeLocation(location: Location) {
    if (this.loggedUser) {
      this.userService.removeLocation(this.loggedUser, location);
    }
  }

  ngOnInit(): void {
    this.authService.loggedUser$.subscribe(user => {
      console.log("Usuario recibido:", user);
      if (user) {
        this.loggedUser = user;
        this.getLocations();
      }
    });

  }

}
