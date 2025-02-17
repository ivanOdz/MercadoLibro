import {Component, Input } from '@angular/core';
import { BookModel } from "../../core/models/bookModel.model";
import { LowerCasePipe, NgForOf } from "@angular/common";
import { TranslatePipe } from "@ngx-translate/core";
import { environment } from "../../../environments/environment";
import { MatIcon } from "@angular/material/icon";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { BookModalComponent } from "./book-modal/book-modal.component";

@Component({
    selector: 'app-book-model-card',
    imports: [
        LowerCasePipe,
        TranslatePipe,
        NgForOf,
        MatIcon,
        FormsModule,
        ReactiveFormsModule,
        BookModalComponent
    ],
    templateUrl: './book-model-card.component.html',
    standalone: true,
    styleUrl: './book-model-card.component.css'
})
export class BookModelCardComponent {

    @Input() bookModel!: BookModel;
    modalVisible: boolean = false;
    bookImage!: string;
    defaultImage: string = './assets/book.jpg';

    ngOnInit() {
        console.log(this.bookModel);
        this.bookImage = this.getBookImage();
    }

    protected getBookImage(): string {
        return this.bookModel.cover ? this.getBaseUrl() + this.bookModel.cover : this.defaultImage;
    }

    getBaseUrl() {
        return `${environment.production? environment.productionUrl  : environment.developmentUrl}`;
    }

    getFormattedAuthors(authors: string[]): string {
        const maxAuthors = 3;
        if (authors.length > maxAuthors) {
            return authors.slice(0, maxAuthors).join(', ') + ', ...';
        }
        return authors.join(', ');
    }

    getStarArray(averageRating: number): string[] {
        const fullStars = Math.floor(averageRating);
        const hasHalfStar = averageRating % 1 !== 0;
        const emptyStars = 5 - fullStars - (hasHalfStar ? 1 : 0);

        return [
            ...Array(fullStars).fill('star'),
            ...(hasHalfStar ? ['star_half'] : []),
            ...Array(emptyStars).fill('star_border')
        ];
    }

    openModal() {
        this.modalVisible = true;
    }

    closeModal() {
        this.modalVisible = false;
    }

}
