import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from './auth.service';
import { filter, switchMap, map } from 'rxjs/operators';
import { BehaviorSubject, Observable } from 'rxjs';
import {UserService} from "./user.service";

@Injectable({
    providedIn: 'root'
})
export class LanguageService {
    private currentLanguageSubject = new BehaviorSubject<string>('en');
    currentLanguage$: Observable<string> = this.currentLanguageSubject.asObservable();

    constructor(private translate: TranslateService, private authService: AuthService, private userService: UserService) {
        this.translate.addLangs(['en', 'es']);

        this.authService.ready$.pipe(
            filter(ready => ready),
            switchMap(() => this.authService.loggedUser$),
            map(user => user?.language?.split('-')[0] || this.getBrowserLanguage())
        ).subscribe(lang => {
            this.setLanguage(lang);
        });

        // Inicializar el idioma desde TranslateService
        this.currentLanguageSubject.next(this.translate.currentLang || this.getBrowserLanguage());

        // Suscribirse a cambios de idioma dentro de TranslateService
        this.translate.onLangChange.subscribe(({ lang }) => {
            this.currentLanguageSubject.next(lang);
            this.updateUserLanguage(lang);
        });
    }

    private getBrowserLanguage(): string {
        const browserLang = this.translate.getBrowserLang()?.split('-')[0];
        return browserLang === 'es' ? 'es' : 'en';
    }

    setLanguage(lang: string) {
        this.translate.use(lang);
        this.currentLanguageSubject.next(lang);
        this.updateUserLanguage(lang);
    }

    private updateUserLanguage(language: string) {
        this.authService.loggedUser$.pipe(
            filter(user => user != null), // Asegurarse de que el usuario está logueado
            switchMap(user => this.userService.updateLanguage(user, language))
        ).subscribe({
            next: () => console.log(`Language updated to ${language}`),
            error: (err) => console.error('Error updating language', err)
        });
    }

    getCurrentLanguage(): string {
        return this.currentLanguageSubject.getValue();
    }
}
