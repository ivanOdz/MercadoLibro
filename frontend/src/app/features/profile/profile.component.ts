// profile.component.ts
import { Component, OnInit } from '@angular/core';
import {User} from "../../core/models/user.model";
import {AuthService} from "../../core/services/auth.service";
import {HttpClient} from "@angular/common/http";
import {NavbarComponent} from "../../shared/components/navbar/navbar.component";  // Ajusta la importación según tu estructura

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  standalone: true,
  imports: [
    NavbarComponent
  ],
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{
  loggedUser: User | null = null;

  constructor(private authService: AuthService, private http: HttpClient) {

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