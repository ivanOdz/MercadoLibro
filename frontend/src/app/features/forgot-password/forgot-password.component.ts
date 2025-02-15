import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {ButtonDirective} from "primeng/button";
import {UserService} from "../../core/services/user.service";
import {NgIf} from "@angular/common";
import {LanguageService} from "../../core/services/language.service";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-forgot-password',
  imports: [
    FormsModule,
    InputText,
    ButtonDirective,
    NgIf,
    TranslatePipe
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
              private translate: TranslateService,
              private languageService: LanguageService // DO NOT DELETE! Translation would not work otherwise
  ) {}

  sendRecoveryCode() {
    this.userService.changePasswordRequest(this.email).subscribe({
      next: ()=> {
        this.showForm = false;
        this.showConfirmation = true;
        this.message = this.translate.instant('AUTH.CHECK_INBOX');
      },
      error: () => {
        // IMPLEMENT: ERROR MANAGEMENT
    }
    });
  }

}
