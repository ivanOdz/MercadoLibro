import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
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
	rating: number = 1;
	
	constructor(private formBuilder: FormBuilder, private translate: TranslateService, private location: Location, private router: Router) {
		this.bookForm = this.formBuilder.group({
													isbn: ['978', [Validators.required, Validators.pattern(/^(97[89])\d{1,5}\d{1,7}\d{1,7}\d$/)]],
													title: ['', Validators.required],
													editorial: ['', [Validators.required, Validators.pattern('^(?!\\d+$).+')]],
													description: [''],
													genre: ['', Validators.required],
													edition: [1, [Validators.required, Validators.min(1), Validators.max(99), Validators.max(99999)]],
													weight: [300, [Validators.required, Validators.min(1), Validators.max(99999)]],
													pages: [80, [Validators.min(1), Validators.max(99999)]],
													bookLanguage: ['es', Validators.required],
													dimension: [''],
													publicationYear: [new Date().getFullYear(), [Validators.required, Validators.min(999), Validators.pattern('^[0-9]*$'), Validators.max(new Date().getFullYear())]],
													isPocketEdition: [false],
													isHardcover: [false],
													rating: [1, [Validators.min(1), Validators.max(5)]],
													authors: this.formBuilder.array([""]),
												});
		this.translate.setDefaultLang('en');
	}
	
	goBack() {

		if (window.history.length > 2) {
			this.location.back();
		}
		else {
			this.router.navigate(['/']);
		}
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
	
	setRating(value: number): void {
		this.rating = value;
		this.bookForm.get('rating')?.setValue(value);
	}
	
	submitForm() {
		
		if (this.authors.length > 1)
		{
			this.authors.controls.forEach((control, index) => {
				if (!control.value.trim()) {
					this.authors.removeAt(index);
				}
			});
		}
		
		if (this.bookForm.valid) {
			console.log('New book:', this.bookForm.value);
			
			
		}
	}
}
