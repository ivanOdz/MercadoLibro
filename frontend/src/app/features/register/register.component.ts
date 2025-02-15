import { Component } from '@angular/core';
import {ButtonDirective} from "primeng/button";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {Password} from "primeng/password";
import {AuthService} from "../../core/services/auth.service";
import {NgIf} from "@angular/common";
import {LanguageService} from "../../core/services/language.service";
import {Router} from "@angular/router";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ButtonDirective,
    FormsModule,
    InputText,
    Password,
    ReactiveFormsModule,
    NgIf,
    TranslatePipe
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
              private router: Router,
              private languageService: LanguageService // DO NOT DELETE! Translation would not work otherwise
  ) {}

  register() {
    this.authService.register(this.email, this.username, this.password).subscribe({
      next: () => {
        this.router.navigate(['/auth/register/success']);
      },
      error: (err) => {
        console.error('Error al registrar:', err);
      }
    });
  }
}
