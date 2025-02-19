import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { CommonModule, NgForOf } from '@angular/common';
import { User } from '../../core/models/user.model';
import { UserService } from '../../core/services/user.service';
import { AuthService } from '../../core/services/auth.service';
import { FormsModule } from '@angular/forms';
import { Location } from '../../core/models/location.model';
import { ReviewComponent } from './review/review.component';
import { Review } from '../../core/models/review.model';
import { TranslatePipe, TranslateService } from "@ngx-translate/core";
import { Pagination } from "../../core/models/pagination";
import { PaginatorComponent } from "../../shared/paginator/paginator.component";
import {MatIcon} from "@angular/material/icon";
import {environment} from "../../../environments/environment";
import {Button} from "primeng/button";
import {Dialog} from "primeng/dialog";
import {catchError} from "rxjs";

@Component({
  selector: 'app-profile',
  imports: [NavbarComponent, NgForOf, FormsModule, CommonModule, ReviewComponent, TranslatePipe, PaginatorComponent, MatIcon, Button, Dialog],
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  url = environment.production ? environment.productionUrl : environment.developmentUrl;
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
  pagination: Pagination | null = null;


  constructor(private authService: AuthService, private translate: TranslateService) {}

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
          this.usernameError = '';
          this.closeModal();
        },
        error: (err) => {
          this.usernameError = err.message;
        }
      });
    }
  }

  getLocations() {
    if (this.loggedUser) {
      this.userService.getLocations(this.loggedUser).subscribe(locations => {
        this.locations = locations;
      });
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
      this.userService.addLocation(this.loggedUser, location).subscribe({
        next: () => {
          this.locations.push({location: location, publications: '', self: ''});
          this.closeAddModal();
          this.getLocations();
        },
        error: (err) => {
          this.closeAddModal();
        }
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
      this.userService.removeLocation(this.locationToRemove).subscribe( {
        next: () => {
          this.locations = this.locations.filter(loc => loc !== this.locationToRemove);
          this.closeRemoveModal();
        },
        error: (err) => {
          this.closeRemoveModal();
        }
      });
    }
  }

  getReviews(url: string | null = null) {
    if (this.loggedUser) {
      const requestUrl = url || `${this.loggedUser.reviews}?page=0`;
      this.userService.getReviewsFromUrl(requestUrl).subscribe(({ reviews, pagination }) => {
        this.reviews = reviews;
        this.pagination = pagination;
      });
    }
  }

  ngOnInit(): void {
    this.authService.loggedUser$.subscribe(user => {
      if (user) {
        this.loggedUser = user;
        this.getLocations();
        this.getReviews();
      }
    });

  }

}
