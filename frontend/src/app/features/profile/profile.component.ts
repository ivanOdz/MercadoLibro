import { Component, inject, OnInit } from '@angular/core';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { NgForOf, NgOptimizedImage } from '@angular/common';
import { User } from '../../core/models/user.model';
import { UserService } from '../../core/services/user.service';
import { AuthService } from "../../core/services/auth.service";
import { HttpClient } from "@angular/common/http";

@Component({
  selector: 'app-profile',
  imports: [NavbarComponent, NgOptimizedImage, NgForOf],
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  loggedUser: User | null = null;
  userServices: UserService = inject(UserService);


  constructor(private authService: AuthService, private http: HttpClient) {
    //this.user = this.userServices.getUser();
  }

  ngOnInit(): void {
    this.authService.loggedUser$.subscribe(user => {
      if (!user) {
        console.log("No hay usuario logueado, se cancela la petición.");
        return; // Evita ejecutar la petición si user es null o undefined
      }

      this.loggedUser = user;
      console.log("Intento de obtener las ubicaciones del usuario logueado: " + user.username);

      this.http.get("http://localhost:8080/api" + this.loggedUser.locations)
          .subscribe((locations) => {
            console.log(locations);
          });
    });
  }

}
