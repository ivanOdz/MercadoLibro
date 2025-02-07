import { Component } from '@angular/core';
import {ButtonDirective} from "primeng/button";
import {Checkbox} from "primeng/checkbox";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {Password} from "primeng/password";
import {AuthService} from "../../core/services/auth.service";
import {UserService} from "../../core/services/user.service";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ButtonDirective,
    Checkbox,
    FormsModule,
    InputText,
    Password,
    ReactiveFormsModule
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  email: string = '';
  username: string = '';
  password: string = '';
  repeatPassword: string = '';

  constructor(private authService: AuthService) {}

  register() {
    this.authService.register(this.email, this.username, this.password).subscribe({
      next: (location) => {
        if (location) {
          console.log('Registro exitoso, redirigiendo a:', location);
        }
      },
      error: (err) => {
        console.error('Error al registrar:', err);
      }
    });
  }
}
