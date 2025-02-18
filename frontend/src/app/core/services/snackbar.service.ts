import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import {TranslateService} from "@ngx-translate/core";

@Injectable({
    providedIn: 'root'
})
export class SnackbarService {
    constructor(private snackBar: MatSnackBar, private translate: TranslateService) {}

    showError(message: string, action: string = 'Cerrar'): void {
        this.translate.get(message).subscribe((translatedMessage: string) => {
            this.snackBar.open(translatedMessage, action, {
                duration: 5000,
                horizontalPosition: 'center',
                verticalPosition: 'bottom',
                panelClass: ['snackbar-error', 'snackbar-large', 'snack-bar-button']
            });
        });
    }
}
