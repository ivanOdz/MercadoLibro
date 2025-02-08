import { Component } from '@angular/core';
import { LanguageService } from "../../../core/services/language.service";
import { DropdownModule } from "primeng/dropdown";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";

@Component({
  selector: 'app-language-switcher',
  template: `
    <p-dropdown
        [options]="languages"
        [(ngModel)]="selectedLanguage"
        optionLabel="label"
        (onChange)="changeLanguage($event.value)"
        placeholder="Select a language"
    >
      <ng-template let-item pTemplate="selectedItem">
        <div class="flex align-items-center gap-2">
          <span class="fi fi-{{ item.value === 'en' ? 'us' : item.value }}"></span>
          <span>{{ item.label }}</span>
        </div>
      </ng-template>
      <ng-template let-item pTemplate="item">
        <div class="flex align-items-center gap-2">
          <span class="fi fi-{{ item.value === 'en' ? 'us' : item.value }}"></span>
          <span>{{ item.label }}</span>
        </div>
      </ng-template>
    </p-dropdown>
  `,
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

  selectedLanguage: { label: string, value: string } = this.languages[0]; // Idioma por defecto

  constructor(private translate: LanguageService) {}

  changeLanguage(selectedLanguage: { label: string, value: string }) {
    this.translate.changeLanguage(selectedLanguage.value);
  }
}