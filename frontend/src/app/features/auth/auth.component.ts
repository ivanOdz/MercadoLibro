import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { Password } from 'primeng/password';
import { InputText } from 'primeng/inputtext';
import { Checkbox } from 'primeng/checkbox';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import {LanguageSwitcherComponent} from "../../shared/components/language-switcher/language-switcher.component";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [ButtonModule, InputText, Password, Checkbox, FormsModule, LanguageSwitcherComponent, TranslatePipe],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css',
})
export class AuthComponent {
  username: string = '';
  password: string = '';
  rememberMe: boolean = false;

  constructor(
      private authService: AuthService,
      private router: Router,
      private route: ActivatedRoute,
  ) {}

  login() {
    // Manda las credenciales para obtener al user
    this.authService.login(this.username, this.password, this.rememberMe);

    // Vuelve a la pagina en la que se produjo el 401.
    const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.router.navigateByUrl(returnUrl);
  }
}