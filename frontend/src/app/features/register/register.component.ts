import {ChangeDetectorRef, Component} from '@angular/core';
import { ButtonDirective } from "primeng/button";
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from "@angular/forms";
import { InputText } from "primeng/inputtext";
import { AuthService } from "../../core/services/auth.service";
import { NgIf } from "@angular/common";
import { Router } from "@angular/router";
import { TranslatePipe } from "@ngx-translate/core";
import {LanguageService} from "../../core/services/language.service";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    ButtonDirective,
    FormsModule,
    InputText,
    ReactiveFormsModule,
    NgIf,
    TranslatePipe
  ],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  isRegistered = false;
  loginForm: FormGroup;
  registrationError = false;
  registrationMessage = 'AUTH.ERROR_REGISTRATION';

  constructor(
      private authService: AuthService,
      private router: Router,
      private fb: FormBuilder,
      private chRef: ChangeDetectorRef,
      private languageService: LanguageService // DO NOT DELETE! Translation would not work otherwise
  ) {
    this.loginForm = this.fb.group(
        {
          email: ['', [Validators.required, Validators.email]],
          username: ['', [Validators.required, Validators.minLength(5)]],
          password: ['', [Validators.required]],
          repeatPassword: ['', [Validators.required]]
        },
        { validators: this.passwordsMatchValidator }
    );
  }

  register() {
    if (this.loginForm.invalid) {
      return;
    }

    const { email, username, password } = this.loginForm.value;

    this.authService.register(email, username, password).subscribe({
      next: () => {
        this.router.navigate(['/auth/register/success']);
      },
      error: (err) => {
        this.registrationError = true
        this.registrationMessage = err.message
        this.chRef.detectChanges()
      }
    });
  }

  private passwordsMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value;
    const repeatPassword = group.get('repeatPassword')?.value;
    return password === repeatPassword ? null : { passwordsMismatch: true };
  }
}
