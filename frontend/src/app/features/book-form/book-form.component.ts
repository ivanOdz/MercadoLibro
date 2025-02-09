import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { NavbarComponent } from "../../shared/components/navbar/navbar.component";
import { BookModel } from "../../core/models/bookModel.model";
import { BookmodelService } from "../../core/services/bookmodel.service";

@Component({
	
	selector: 'app-book-form',
	standalone: true,
	imports: [CommonModule, ReactiveFormsModule, NavbarComponent, TranslatePipe],
	templateUrl: './book-form.component.html',
	styleUrls: ['./book-form.component.css'],
})
export class BookFormComponent {
	
	url: string = "http://localhost:8080/api/book_models";
	bookModelService: BookmodelService = inject(BookmodelService);
	bookModelForm: FormGroup;
	rating: number = 1;
	imagePreview: string | ArrayBuffer | null = null;
	uploadProgress = 0;
	genres = [	'fiction', 'non.fiction', 'mystery', 'thriller', 'science.fiction', 'fantasy', 'romance', 'historical.fiction', 'horror', 'biography', 'autobiography', 'memoir',
				'young.adult', 'childrens.literature',  'graphic.novel' ,'classic', 'adventure', 'dystopian', 'self.help', 'poetry', 'literary.fiction', 'crime', 'western',
				'contemporary', 'religious.spiritual', 'philosophy', 'science', 'travel', 'true.crime', 'historical.non.fiction', 'other'
			];
	dimensions = ['small', 'medium', 'large'];
	languages = ['spanish', 'english'];
	
	constructor(private formBuilder: FormBuilder, private translate: TranslateService, private location: Location, private router: Router) {
		
		this.bookModelForm = this.formBuilder.group({
													isbn: ['978', [Validators.required, Validators.pattern(/^(97[89])\d{1,5}\d{1,7}\d{1,7}\d$/)]],
													title: ['', Validators.required],
													editorial: ['', [Validators.required, Validators.pattern('^(?!\\d+$).+')]],
													description: [''],
													genre: ['', Validators.required],
													edition: [1, [Validators.required, Validators.min(1), Validators.max(99), Validators.max(99999)]],
													weight: [300, [Validators.required, Validators.min(1), Validators.max(99999)]],
													pages: [80, [Validators.min(1), Validators.max(99999)]],
													bookLanguage: ['spanish', Validators.required],
													dimension: ['medium'],
													publicationYear: [new Date().getFullYear(), [Validators.required, Validators.min(999), Validators.pattern('^[0-9]*$'), Validators.max(new Date().getFullYear())]],
													isPocketEdition: [false],
													isHardcover: [false],
													rating: [1, [Validators.min(1), Validators.max(5)]],
													authors: this.formBuilder.array([""]),
												});
		this.translate.setDefaultLang(this.translate.getBrowserLang() || 'en');
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
		
		return this.bookModelForm.get('authors') as FormArray;
	}
	
	addAuthor() {
		
		this.authors.push(this.formBuilder.control(''));
	}
	
	removeAuthor(index: number) {
		
		this.authors.removeAt(index);
	}
	
	setRating(value: number): void {
		
		this.rating = value;
		this.bookModelForm.get('rating')?.setValue(value);
	}
	
	onImageSelected(event: Event): void {
		
		const file = (event.target as HTMLInputElement).files?.[0];
		
		if (file) {
			
			const reader = new FileReader();
			reader.onload = () => {
				this.imagePreview = reader.result;
				this.bookModelForm.patchValue({ image: file });
			};
			reader.readAsDataURL(file);
		}
	}

	removeImage(): void {
		
		this.imagePreview = null;
		this.bookModelForm.patchValue({ image: null });
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
		
		if (this.bookModelForm.valid) {
			
			console.log('New book:', this.bookModelForm.value);
			
			const bookData = new BookModel(this.bookModelForm.value);
			const rating = this.bookModelForm.value.rating;
			
			this.bookModelService.uploadBookModel(this.url, bookData, rating).subscribe({
				
				next: (response) => {
					console.log('Upload of Book Model successful :)');
				},
				error: (error) => {
					console.error('Upload of Book Model failed', error);
				}
			});
		}
	}
}
