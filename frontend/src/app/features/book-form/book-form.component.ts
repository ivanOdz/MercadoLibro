import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
//import { TranslateModule } from '@ngx-translate/core';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";

@Component({
	selector: 'app-book-form',
	standalone: true,
	imports: [CommonModule, ReactiveFormsModule, NavbarComponent, TranslatePipe],
	templateUrl: './book-form.component.html',
	styleUrls: ['./book-form.component.css'],
})
export class BookFormComponent {
	bookForm: FormGroup;
	
	constructor(private translate: TranslateService, private formBuilder: FormBuilder) {
		this.bookForm = this.formBuilder.group({
													isbn: ['', Validators.required],
													title: ['', Validators.required],
													editorial: ['', Validators.required],
													description: [''],
													genre: ['', Validators.required],
													edition: [1, [Validators.required, Validators.min(1)]],
													weight: [0, [Validators.required, Validators.min(0)]],
													pages: [1, [Validators.required, Validators.min(1)]],
													bookLanguage: ['', Validators.required],
													dimension: [''],
													publicationYear: [2025, [Validators.required, Validators.min(999)]],
													isPocketEdition: [false],
													isHardcover: [false],
													ratingCount: [0, Validators.min(0)],
													averageRating: [0, [Validators.min(0), Validators.max(5)]],
													authors: this.formBuilder.array([]),
												});
		this.translate.setDefaultLang('en');
//		this.translate.use('en');
//		this.translate.reloadLang('en');
	}

	get authors(): FormArray {
		return this.bookForm.get('authors') as FormArray;
	}
	
	addAuthor() {
		this.authors.push(this.formBuilder.control(''));
	}
	
	removeAuthor(index: number) {
		this.authors.removeAt(index);
	}
	
	submitForm() {
		if (this.bookForm.valid) {
			console.log('New book:', this.bookForm.value);
		}
	}
}
