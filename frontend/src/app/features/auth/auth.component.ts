import {ChangeDetectorRef, Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { Password } from 'primeng/password';
import { InputText } from 'primeng/inputtext';
import { Checkbox } from 'primeng/checkbox';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslatePipe } from "@ngx-translate/core";
import { LanguageService } from "../../core/services/language.service";
import {Divider} from "primeng/divider";

@Component({
	selector: 'app-auth',
	standalone: true,
	imports: [CommonModule, ButtonModule, InputText, Password, Checkbox, FormsModule, TranslatePipe, Divider],
	templateUrl: './auth.component.html',
	styleUrl: './auth.component.css',
})
export class AuthComponent {
	username: string = '';
	password: string = '';
	rememberMe: boolean = false;
	errorMessage: string = '';

	error:boolean = false;


  constructor(
	private authService: AuthService,
	private router: Router,
	private route: ActivatedRoute,
	private languageService: LanguageService, // DO NOT DELETE! Translation would not work otherwise
	private cdRef: ChangeDetectorRef
	) { }

	login() {
		this.errorMessage = '';

		this.authService.login(this.username, this.password, this.rememberMe).subscribe({
			next: () => {
				this.authService.isAuthenticated$.subscribe(isAuthenticated => {
					if (isAuthenticated) {
						const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
						this.router.navigateByUrl(returnUrl);
					}
				});
			},
			error: (err) => {
				if (err.status === 401) {
					this.errorMessage = 'INCORRECT_CREDENTIALS';
				} else if (err.status === 403) {
					this.errorMessage = 'NO_PERMISSION';
				} else if (err.status === 500) {
					this.errorMessage = 'SERVER_ERROR';
				} else {
					this.errorMessage = 'UNEXPECTED_ERROR';
				}
				this.error = true;
				this.cdRef.detectChanges();
			}
		});
	}


	goToPublications() {
		this.router.navigate(['/publications']);
	}
}