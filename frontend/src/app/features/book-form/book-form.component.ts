import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators, FormArray, ReactiveFormsModule } from '@angular/forms';
import { CommonModule, Location } from '@angular/common';
import { TranslateService, TranslatePipe } from '@ngx-translate/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { BookModel } from "../../core/models/bookModel.model";
import { BookModelService } from "../../core/services/bookmodel.service";
import {environment} from "../../../environments/environment";
import {BookService} from "../../core/services/book.service";
import {AuthService} from "../../core/services/auth.service";
import {ImageService} from "../../core/services/image.service";

@Component({
	
	selector: 'app-book-form',
	standalone: true,
	imports: [CommonModule, ReactiveFormsModule, NavbarComponent, TranslatePipe],
	templateUrl: './book-form.component.html',
	styleUrls: ['./book-form.component.css'],
})
export class BookFormComponent {
	
	url: string = `${environment.production ? environment.productionUrl : environment.developmentUrl}/book_models`;
	bookUrl: string = (environment.production ? environment.productionUrl : environment.developmentUrl) + '/books';
	bookModelUrl: string = '';
	bookModelService: BookModelService = inject(BookModelService);
	bookService: BookService = inject(BookService);
	authService: AuthService = inject(AuthService);
	user: string | undefined = '';

	bookModelForm: FormGroup;
	rating: number = 1;
	condition: string = '';
	imagePreview: string | ArrayBuffer | null = null;
	selectedFile: File | null = null;
	imageUrl: string | undefined = '';
	uploadProgress = 0;
	genres = [	'fiction', 'non.fiction', 'mystery', 'thriller', 'science.fiction', 'fantasy', 'romance', 'historical.fiction', 'horror', 'biography', 'autobiography', 'memoir',
				'young.adult', 'childrens.literature',  'graphic.novel' ,'classic', 'adventure', 'dystopian', 'self.help', 'poetry', 'literary.fiction', 'crime', 'western',
				'contemporary', 'religious.spiritual', 'philosophy', 'science', 'travel', 'true.crime', 'historical.non.fiction', 'other'
			];
	bookStates = ['new', 'like_new', 'very_good', 'good', 'acceptable', 'worn'];
	dimensions = ['small', 'medium', 'large'];
	languages = ['spanish', 'english'];

	constructor(private formBuilder: FormBuilder, private translate: TranslateService, private location: Location, private router: Router, private imageService: ImageService) {
		
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
													condition: ['', Validators.required],
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

	goBackToBooks() {
		this.router.navigate(['/books']);
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

	onImageSelected(event: any) {
		const file = event.target.files[0];

		if (file) {
			this.selectedFile = file;

			const reader = new FileReader();
			reader.onload = (e: any) => {
				this.imagePreview = e.target.result;
			};
			reader.readAsDataURL(file);
		}
	}

	async onUpload() {
		if (this.selectedFile) {
			try {
				// Esperar a que la imagen se suba
				this.imageUrl = await this.imageService.uploadImage(this.selectedFile).toPromise();
				console.log('Imagen subida correctamente:', this.imageUrl);
			} catch (error) {
				console.error('Error al subir la imagen', error);
			}
		}
	}


	removeImage(): void {
		
		this.imagePreview = null;
		this.selectedFile = null;
	}

	async submitForm() {
		
		this.bookModelForm.markAllAsTouched();
		if (this.bookModelForm.invalid) {
			return;
		}

		if (this.authors.length > 1)
		{
			this.authors.controls.forEach((control, index) => {
				if (!control.value.trim()) {
					this.authors.removeAt(index);
				}
			});
		}

		await this.onUpload();

		if (this.bookModelForm.valid) {

			this.authService.loggedUser$.subscribe(user => {
				this.user = user?.self;
			});

			console.log('New book:', this.bookModelForm.value);
			
			let bookData = new BookModel(this.bookModelForm.value);
			const rating = this.bookModelForm.value.rating;
			console.log('IMAGE URL:', this.imageUrl + '|');
			this.bookModelService.uploadBookModel(this.url, bookData, this.imageUrl).subscribe({

				next: (response) => {
					console.log('Upload of Book Model successful :)');
					const imageArray = this.imageUrl	? [ this.imageUrl ] : [];
					this.bookService.uploadBook(this.bookUrl, response, rating, this.bookModelForm.value.condition, this.user, imageArray ).subscribe({
						next: () => {
							console.log('Upload of Book successful :)');
							this.goBackToBooks();
						},
						error: (error) => {
							console.error('Upload of Book failed', error);
						}
					});
				},
				error: (error) => {
					console.error('Upload of Book Model failed', error);
				}
			});
		}
	}
}
