import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from "../../core/services/user.service";
import {Password} from "primeng/password";
import {FormsModule} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {ButtonDirective} from "primeng/button";
import {LanguageService} from "../../core/services/language.service";

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  standalone: true,
  imports: [
    Password,
    FormsModule,
    InputText,
    ButtonDirective
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
              private languageService: LanguageService // DO NOT DELETE! Translation would not work otherwise
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.passwordCode = params['verification_code'];
      if (!this.passwordCode) {
        this.errorMessage = 'El código de contraseña es inválido o no está presente.';
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
