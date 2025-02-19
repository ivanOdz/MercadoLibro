import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from "../../core/services/user.service";
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {ButtonDirective} from "primeng/button";
import {LanguageService} from "../../core/services/language.service";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  standalone: true,
  imports: [
    FormsModule,
    InputText,
    ButtonDirective,
    TranslatePipe,
    NgIf,
    ReactiveFormsModule
  ],
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  passwordCode: number | undefined;

  errorInChangePassword = false;

  errorInvalidCode = false;

  passwordForm: FormGroup;

  constructor(private userService: UserService,
              private router: Router,
              private route: ActivatedRoute,
              private translate: TranslateService,
              private fb: FormBuilder,
              private cRef: ChangeDetectorRef,
              private languageService: LanguageService // DO NOT DELETE! Translation would not work otherwise
  ) {
    this.passwordForm = this.fb.group(
        {
            verificationCode: ['', [Validators.required]],
            password: ['', [Validators.required]],
            confirmPassword: ['', [Validators.required]]
        },
        { validators: this.passwordsMatchValidator }
        );
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.passwordCode = params['verification_code'];
      this.passwordForm.setValue({verificationCode: this.passwordCode, password: '', confirmPassword: ''});
      if (!this.passwordCode) {
        this.errorInvalidCode = true;
      }
    });
  }

  changePassword() {
    if (this.passwordForm.invalid) {
        return;
    }

    const {verificationCode, password} = this.passwordForm.value;

    this.userService.changePassword(verificationCode, password).subscribe({
      next: () => {
        this.router.navigate(['/auth/login']);
      },
      error: () => {
        this.errorInChangePassword = true;
        this.cRef.detectChanges()
      }
    });
  }


  private passwordsMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value;
    const repeatPassword = group.get('repeatPassword')?.value;
    return password === repeatPassword ? null : { passwordsMismatch: true };
  }
}
