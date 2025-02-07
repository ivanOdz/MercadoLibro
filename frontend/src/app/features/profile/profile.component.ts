import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { CommonModule, NgForOf } from '@angular/common';
import { User } from '../../core/models/user.model';
import { UserService } from '../../core/services/user.service';
import { HttpClient } from "@angular/common/http";
import { AuthService } from '../../core/services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  imports: [NavbarComponent, NgForOf, FormsModule, CommonModule],
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  loggedUser: User | null = null;
  isModalOpen = false;
  newUsername = '';
  userServices: UserService = inject(UserService);
  url = 'http://localhost:8080/api'


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
        this.userServices.updateUsername(this.loggedUser, this.newUsername);
        console.log("Usuario cambiado a: ", this.newUsername);
     }
    }
    this.closeModal();
  }

  updateLanguage(event: Event) {
    const language = (event.target as HTMLSelectElement).value;
    if (this.loggedUser) {
      this.userServices.updateLanguage(this.loggedUser, language);
      console.log("Idioma cambiado a: ", language);
    }
  }

  ngOnInit(): void {
    this.authService.loggedUser$.subscribe(user => {
      console.log("Usuario recibido:", user);
      if (user) {
        this.loggedUser = user;
      }
    });
  }

}
