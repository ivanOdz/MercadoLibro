import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../core/services/user.service";
import {ProgressSpinner} from "primeng/progressspinner";
import {Card} from "primeng/card";
import {Button} from "primeng/button";
import {Message} from "primeng/message";
import {PrimeTemplate} from "primeng/api";

@Component({
  selector: 'app-verify',
  standalone: true,
  imports: [
    ProgressSpinner,
    Card,
    Button,
    Message,
    PrimeTemplate
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
      private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.verificationCode = params['verification_code'];
      if (this.verificationCode) {
        this.verifyUser(this.verificationCode);
      } else {
        this.errorMessage = 'El código de verificación es inválido o no está presente.';
      }
    });
  }

  verifyUser(verificationCode: number): void {
    this.isVerifying = true;
    this.userService.verifyUser(verificationCode).subscribe({
      next: (response) => {
        this.isVerifying = false;
        this.verificationSuccess = true;
      },
      error: (error) => {
        this.isVerifying = false;
        this.verificationFailed = true;
        this.errorMessage = 'El código de verificación no es válido. Por favor, inténtelo nuevamente.';
      }
    });
  }

  goToHome(): void {
    this.router.navigate(['/']);
  }
}
