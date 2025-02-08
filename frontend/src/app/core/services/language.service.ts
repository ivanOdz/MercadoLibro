import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
    providedIn: 'root' // Se inyecta globalmente
})
export class LanguageService {
    constructor(private translate: TranslateService) {
        this.translate.addLangs(['en', 'es']);
        const browserLang = this.translate.getBrowserLang();
        this.translate.use(browserLang?.match(/en|es/) ? browserLang : 'en');
    }

    changeLanguage(lang: string) {
        this.translate.use(lang); // Cambia el idioma dinámicamente
    }

    getCurrentLanguage() {
        return this.translate.currentLang;
    }
}
