import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {ButtonDirective} from "primeng/button";
import {UserService} from "../../core/services/user.service";
import {NgIf} from "@angular/common";
import {LanguageService} from "../../core/services/language.service";

@Component({
  selector: 'app-forgot-password',
  imports: [
    FormsModule,
    InputText,
    ButtonDirective,
    NgIf
  ],
  standalone: true,
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  email: string = '';
  message: string = '';
  showConfirmation: boolean = false;
  showForm: boolean = true;

  constructor(private userService: UserService,
              private languageService: LanguageService // DO NOT DELETE! Translation would not work otherwise
  ) {}

  sendRecoveryCode() {
    this.userService.changePasswordRequest(this.email).subscribe({
      next: ()=> {
        this.showForm = false;
        this.showConfirmation = true;
        this.message = 'Si el correo está registrado, revisa tu bandeja de entrada para el código de recuperación.';
      },
      error: () => {
        this.showForm = false;
        this.showConfirmation = true;
        this.message = 'Si el correo está registrado, revisa tu bandeja de entrada para el código de recuperación.';
    }
    });
  }
}
