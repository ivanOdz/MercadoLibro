import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { providePrimeNG } from 'primeng/config';


import { routes } from './app.routes';
import { MyPreset } from "../styles";
import { HttpClient, provideHttpClient, withInterceptors } from "@angular/common/http";
import { authInterceptor } from "./core/interceptors/auth.interceptor";
import { TranslateLoader, TranslateModule } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";


export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export const appConfig: ApplicationConfig = {
  providers: [provideAnimationsAsync(),
              providePrimeNG({ theme: { preset: MyPreset, options: {darkModeSelector: '.my-app-dark' } } }),
              provideZoneChangeDetection({ eventCoalescing: true }),
              provideRouter(routes),
              provideHttpClient(withInterceptors([authInterceptor])),
              importProvidersFrom(
                  TranslateModule.forRoot({
                      loader: {
                          provide: TranslateLoader,
                          useFactory: HttpLoaderFactory,
                          deps: [HttpClient]
                      }
                  })
              ),
			]
};

