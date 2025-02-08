import { Component } from '@angular/core';
import { LanguageService } from "../../../core/services/language.service";
import { DropdownModule } from "primeng/dropdown";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import {translate} from "@angular/localize/tools";

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

  selectedLanguage: { label: string, value: string };

  constructor(private translate: LanguageService) {
    this.selectedLanguage = this.translate.getCurrentLanguage() === 'en' ? this.languages[0] : this.languages[1];
  }

  changeLanguage(selectedLanguage: { label: string, value: string }) {
    this.translate.changeLanguage(selectedLanguage.value);
  }
}