import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import {LanguageService} from "../../core/services/language.service";

@Component({
    selector: 'app-register-success',
    standalone: true,
    imports: [
        FormsModule,
        ReactiveFormsModule,
        TranslatePipe
    ],
    templateUrl: './register-success.component.html',
    styleUrl: './register.component.css'
})
export class RegisterComponentSuccess {

    email: string = '';
    username: string = '';
    password: string = '';

    constructor(private languageService: LanguageService // DO NOT DELETE! Translation would not work otherwise
    ) {}

}
