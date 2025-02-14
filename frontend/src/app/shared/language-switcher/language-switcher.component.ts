import { Component } from '@angular/core';
import { LanguageService } from "../../core/services/language.service";
import { DropdownModule } from "primeng/dropdown";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { Observable } from 'rxjs';
import {map} from "rxjs/operators";

@Component({
  selector: 'app-language-switcher',
  templateUrl: './language-switcher.component.html',
  standalone: true,
  imports: [
    DropdownModule,
    FormsModule,
    CommonModule
  ],
  styleUrls: ['./language-switcher.component.css']
})
export class LanguageSwitcherComponent {
  languages = [
    { label: 'English', value: 'en' },
    { label: 'Español', value: 'es' }
  ];

  selectedLanguage$!: Observable<{ label: string, value: string }>;

  constructor(private languageService: LanguageService) {
    this.selectedLanguage$ = this.languageService.currentLanguage$.pipe(
        map(lang => this.languages.find(l => l.value === lang) || this.languages[0])
    );
  }

  changeLanguage(selectedLanguage: { label: string, value: string }) {
    this.languageService.setLanguage(selectedLanguage.value);
  }
}
