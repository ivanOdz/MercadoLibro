import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';

@Injectable({ providedIn: 'root' })
export class SnackbarService {
    constructor(private snackBar: MatSnackBar, private translate: TranslateService) {}

    showError(message: string): void {
        this.translate.get(message).subscribe((translatedMessage: string) => {
            this.snackBar.open(translatedMessage, 'Cerrar', {
                duration: 5000,
                horizontalPosition: 'center',
                verticalPosition: 'bottom',
                panelClass: ['custom-snackbar-error'],
            });
        });
    }

    showSuccess(message: string): void {
        this.translate.get(message).subscribe((translatedMessage: string) => {
            this.snackBar.open(translatedMessage, 'Cerrar', {
                duration: 5000,
                horizontalPosition: 'center',
                verticalPosition: 'bottom',
                panelClass: ['custom-snackbar-success'],
            });
        });
    }
}
