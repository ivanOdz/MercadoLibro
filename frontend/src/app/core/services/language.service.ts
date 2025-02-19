import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from './auth.service';
import {filter, switchMap, map, take} from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserService } from './user.service';
import {SnackbarService} from "./snackbar.service";

@Injectable({
    providedIn: 'root'
})
export class LanguageService {
    private currentLanguageSubject = new BehaviorSubject<string>('en');
    currentLanguage$: Observable<string> = this.currentLanguageSubject.asObservable();

    constructor(private translate: TranslateService, private authService: AuthService, private userService: UserService, private snackBarService: SnackbarService) {
        this.translate.addLangs(['en', 'es']);

        this.authService.ready$.pipe(
            filter(ready => ready),
            switchMap(() => this.authService.loggedUser$),
            map(user => user?.language?.split('-')[0] || this.getBrowserLanguage())
        ).subscribe(lang => {
            this.setLanguage(lang, false);
        });

        // Inicializar el idioma desde TranslateService
        this.currentLanguageSubject.next(this.translate.currentLang || this.getBrowserLanguage());

        // Suscribirse a cambios de idioma dentro de TranslateService
        this.translate.onLangChange.subscribe(({ lang }) => {
            this.currentLanguageSubject.next(lang);
        });
    }

    private getBrowserLanguage(): string {
        const browserLang = this.translate.getBrowserLang()?.split('-')[0];
        return browserLang === 'es' ? 'es' : 'en';
    }

    setLanguage(lang: string, updateUser = true) {
        this.translate.use(lang);

        if (updateUser) {
            this.updateUserLanguage(lang);
        }
        this.currentLanguageSubject.next(lang);
    }

    private updateUserLanguage(language: string) {
        this.authService.loggedUser$.pipe(
            take(1),
            filter(user => user != null && user.language !== language), // Asegurarse de que el usuario está logueado
            switchMap(user => this.userService.updateLanguage(user, language))
        ).subscribe({
            //error: (err) => this.snackBarService.showError("ERROR.CHANGE_LANGUAGE")
        });
    }

    getCurrentLanguage(): string {
        return this.currentLanguageSubject.getValue();
    }
}
