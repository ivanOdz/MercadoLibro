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
import { Review } from '../../core/models/review.model';
import { LanguageSwitcherComponent } from "../../shared/components/language-switcher/language-switcher.component";
import { TranslatePipe, TranslateService } from "@ngx-translate/core";

@Component({
  selector: 'app-profile',
  imports: [NavbarComponent, NgForOf, FormsModule, CommonModule, ReviewComponent, LanguageSwitcherComponent, TranslatePipe],
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  url = 'http://localhost:8080/api'
  userService: UserService = inject(UserService);

  loggedUser: User | null = null;
  newUsername = '';
  newLocationName: string = "";
  usernameError: string = '';
  locations: Location[] = [];
  reviews: Review[] = [];

  isModalOpen = false;
  isAddModalOpen = false;
  isRemoveModalOpen = false;
  locationToRemove: Location | null = null;



  constructor(private authService: AuthService, private translate: TranslateService, ) {}

  openModal() {
    if (this.loggedUser) {
      this.newUsername = this.loggedUser.username;
    }
    this.isModalOpen = true;
  }

  closeModal() {
    if (this.loggedUser) {
      this.newUsername = this.loggedUser.username;
    }
    this.isModalOpen = false;
  }

  isValidUsername(): boolean {
    return this.newUsername.trim().length > 0;
  }

  clearErrorMessage() {
    this.usernameError = '';
  }

  updateUsername() {
    if (!this.newUsername.trim()) {
      this.usernameError = this.translate.instant("PROFILE.USERNAME_EMPTY");
      return;
    }
    if (this.newUsername.trim() && this.loggedUser) {
      if(this.newUsername === this.loggedUser.username){
        this.usernameError = '';
        this.closeModal();
        return;
      }
      this.userService.updateUsername(this.loggedUser, this.newUsername).subscribe({
        next: () => {
          if(this.loggedUser){
            this.loggedUser.username = this.newUsername;
          }
          console.log("Usuario cambiado a:", this.newUsername);
          this.usernameError = '';
          this.closeModal();
        },
        error: (err) => {
          this.usernameError = err.message;
          console.log("Usuario en uso:", this.newUsername);
        }
      });
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

  openAddModal() {
    this.newLocationName = "";
    this.isAddModalOpen = true;
  }

  closeAddModal() {
    this.isAddModalOpen = false;
  }

  addLocation(location: string) {
    if (this.loggedUser && location.trim() !== '') {
      this.userService.addLocation(this.loggedUser, location).subscribe(() => {
        this.locations.push({ location: location, publications: '', self: '' });
        this.closeAddModal();
        this.getLocations();
      });
    }
  }

  openRemoveModal(location: Location) {
    this.locationToRemove = location;
    this.isRemoveModalOpen = true;
  }

  closeRemoveModal() {
    this.isRemoveModalOpen = false;
    this.locationToRemove = null;
  }

  removeLocation() {
    if (this.locationToRemove && this.loggedUser) {
      this.userService.removeLocation(this.loggedUser, this.locationToRemove).subscribe(() => {
        this.locations = this.locations.filter(loc => loc !== this.locationToRemove);
        this.closeRemoveModal();
      });
    }
  }

  getReviews() {
    if (this.loggedUser) {
      this.userService.getReviews(this.loggedUser).subscribe(reviews => {
        this.reviews = reviews;
      });
    } else {
      console.log("el usuario no está definido, no se pueden cargar las reseñas");
    }
  }

  ngOnInit(): void {
    this.authService.loggedUser$.subscribe(user => {
      console.log("Usuario recibido:", user);
      if (user) {
        this.loggedUser = user;
        this.getLocations();
        this.getReviews();
      }
    });

  }

}
