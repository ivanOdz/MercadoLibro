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
import {LanguageSwitcherComponent} from "../../shared/components/language-switcher/language-switcher.component";

@Component({
  selector: 'app-profile',
  imports: [NavbarComponent, NgForOf, FormsModule, CommonModule, ReviewComponent, LanguageSwitcherComponent],
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  url = 'http://localhost:8080/api'
  userService: UserService = inject(UserService);

  loggedUser: User | null = null;
  newUsername = '';
  locations: Location[] = [
    {
      "location": "Buenos Aires, Argentina",
      "publications": "/publications?location=1",
      "self": "/locations/1"
    },
    {
      "location": "Madrid, España",
      "publications": "/publications?location=2",
      "self": "/locations/2"
    },
    {
      "location": "Ciudad de México, México",
      "publications": "/publications?location=3",
      "self": "/locations/3"
    },
    {
      "location": "Bogotá, Colombia",
      "publications": "/publications?location=4",
      "self": "/locations/4"
    },
    {
      "location": "Santiago, Chile",
      "publications": "/publications?location=5",
      "self": "/locations/5"
    }
  ];

  reviews: Review[] = [
    {
      "description": "Excelente intercambio, el libro estaba en perfectas condiciones.",
      "reviewDate": "2024-02-07T10:30:00Z",
      "rating": 5,
      "self": "/reviews/1",
      "subject": "/users/2",
      "reviewer": "/users/5",
      "exchange": "/exchanges/12"
    },
    {
      "description": "El libro tenía algunas marcas pero en general todo bien.",
      "reviewDate": "2024-02-06T15:45:00Z",
      "rating": 4,
      "self": "/reviews/2",
      "subject": "/users/3",
      "reviewer": "/users/7",
      "exchange": "/exchanges/15"
    },
    {
      "description": "Hubo demoras en la entrega, pero el libro estaba bien.",
      "reviewDate": "2024-02-05T18:20:00Z",
      "rating": 3,
      "self": "/reviews/3",
      "subject": "/users/8",
      "reviewer": "/users/4",
      "exchange": "/exchanges/20"
    },
    {
      "description": "Excelente intercambio, el libro estaba en perfectas condiciones.",
      "reviewDate": "2024-02-07T10:30:00Z",
      "rating": 5,
      "self": "/reviews/1",
      "subject": "/users/2",
      "reviewer": "/users/5",
      "exchange": "/exchanges/12"
    },

  ];

  isModalOpen = false;
  isRemoveModalOpen = false;
  locationToRemove: Location | null = null;



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

  openRemoveModal(location: Location) {
    this.locationToRemove = location;
    this.isRemoveModalOpen = true;
  }

  closeRemoveModal() {
    this.isRemoveModalOpen = false;
    this.locationToRemove = null;
  }

  removeLocation(location: Location | null) {
    if (this.loggedUser) {
      if(location) {
        this.userService.removeLocation(this.loggedUser, location);
      }
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
