import { Component } from '@angular/core';
import {ButtonDirective} from "primeng/button";
import {Checkbox} from "primeng/checkbox";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {Password} from "primeng/password";
import {AuthService} from "../../core/services/auth.service";
import {UserService} from "../../core/services/user.service";
import {NgIf} from "@angular/common";
import {LanguageService} from "../../core/services/language.service";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ButtonDirective,
    FormsModule,
    InputText,
    Password,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  email: string = '';
  username: string = '';
  password: string = '';
  repeatPassword: string = '';
  isRegistered = false;

  constructor(private authService: AuthService,
              private languageService: LanguageService // DO NOT DELETE! Translation would not work otherwise
  ) {}

  register() {
    this.authService.register(this.email, this.username, this.password).subscribe({
      next: () => {
        this.isRegistered = true;
      },
      error: (err) => {
        console.error('Error al registrar:', err);
      }
    });
  }
}
