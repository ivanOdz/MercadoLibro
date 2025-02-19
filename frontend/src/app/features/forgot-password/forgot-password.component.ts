import {ChangeDetectorRef, Component} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
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
    TranslatePipe,
    ReactiveFormsModule
  ],
  standalone: true,
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {
  emailForm: FormGroup;

  message: string = '';
  showConfirmation: boolean = false;
  showForm: boolean = true;
  errorNoSuchUser: boolean = false;

  constructor(private userService: UserService,
              private translate: TranslateService,
              private languageService: LanguageService, // DO NOT DELETE! Translation would not work otherwise
              private fb: FormBuilder,
              private ref: ChangeDetectorRef
  ) {
    this.emailForm = this.fb.group(
        {
          email: ['', [Validators.required, Validators.email]],
        }
    );
  }

  sendRecoveryCode() {
    if (this.emailForm.invalid) {
        return;
    }

    const {email} = this.emailForm.value
    this.userService.changePasswordRequest(email).subscribe({
      next: ()=> {
        this.showForm = false;
        this.showConfirmation = true;
        this.message = this.translate.instant('AUTH.CHECK_INBOX');
      },
      error: () => {
        this.errorNoSuchUser = true
        this.ref.detectChanges();
    }
    });
  }

}
