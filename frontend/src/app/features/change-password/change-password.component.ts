import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from "../../core/services/user.service";
import {Password} from "primeng/password";
import {FormsModule} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {ButtonDirective} from "primeng/button";
import {LanguageService} from "../../core/services/language.service";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  standalone: true,
  imports: [
    Password,
    FormsModule,
    InputText,
    ButtonDirective,
    TranslatePipe
  ],
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  passwordCode: number | undefined;
  newPassword: string = '';
  confirmPassword: string = '';
  errorMessage: string = '';

  constructor(private userService: UserService,
              private router: Router,
              private route: ActivatedRoute,
              private translate: TranslateService,
              private languageService: LanguageService // DO NOT DELETE! Translation would not work otherwise
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.passwordCode = params['verification_code'];
      if (!this.passwordCode) {
        this.errorMessage = this.translate.instant('AUTH.INVALID_CODE');
      }
    });
  }

  changePassword() {
    if (this.newPassword === this.confirmPassword) {
      this.userService.changePassword(this.passwordCode, this.newPassword).subscribe({
        next: () => {
          this.router.navigate(['/auth/login']);
        },
        error: (err) => {
          console.error('Error al cambiar la contraseña:', err);
        }
      });
    } else {
      console.error('Las contraseñas no coinciden');
    }
  }
}
