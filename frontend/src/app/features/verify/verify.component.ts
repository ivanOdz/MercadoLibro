import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../core/services/user.service";
import {ProgressSpinner} from "primeng/progressspinner";
import {Card} from "primeng/card";
import {Button} from "primeng/button";
import {Message} from "primeng/message";
import {PrimeTemplate} from "primeng/api";
import {NgIf} from "@angular/common";
import {LanguageService} from "../../core/services/language.service";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-verify',
  standalone: true,
  imports: [
    ProgressSpinner,
    Card,
    Button,
    Message,
    PrimeTemplate,
    NgIf,
    TranslatePipe
  ],
  templateUrl: './verify.component.html',
  styleUrl: './verify.component.css'
})

export class VerifyComponent implements OnInit {
  verificationCode: number | null = null;
  isVerifying: boolean = false;
  verificationSuccess: boolean = false;
  errorMessage: string = '';
  verificationFailed: boolean = false;

  constructor(
      private route: ActivatedRoute,
      private userService: UserService,
      private router: Router,
      private translate: TranslateService,
      private languageService: LanguageService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.verificationCode = params['verification_code'];
      if (this.verificationCode) {
        this.verifyUser(this.verificationCode);
      } else {
        this.errorMessage = this.translate.instant('AUTH.VERIFICATION_CODE_MISSING');
      }
    });
  }

  verifyUser(verificationCode: number): void {
    this.isVerifying = true;
    this.userService.verifyUser(verificationCode).subscribe({
      next: () => {
        this.isVerifying = false;
        this.verificationSuccess = true;
      },
      error: () => {
        this.isVerifying = false;
        this.verificationFailed = true;
        this.errorMessage = this.translate.instant('AUTH.VERIFICATION_CODE_MISSING');
      }
    });
  }

  goToHome(): void {
    this.router.navigate(['/publications']);
  }
}
