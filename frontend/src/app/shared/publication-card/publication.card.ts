import { Component, Input, OnInit } from "@angular/core";
import { PublicationData } from "../../core/models/types";
import { Router } from "@angular/router";
import { TranslatePipe } from '@ngx-translate/core';
import { environment } from "../../../environments/environment";

@Component({
    selector: 'publication-card',
    templateUrl: './publication.card.html',
    styleUrl: './publication.card.css',
    standalone: true,
	imports: [TranslatePipe]
})
export class PublicationCardComponent implements OnInit {
    @Input() publication!: PublicationData;
	bookImage!: string;
	defaultImage: string = './assets/book.jpg';
	
    constructor(private router: Router) { }

	ngOnInit() {
		this.bookImage = this.getBookImage();
	}

    goToPublicationDetail() {
        if (this.publication.self) {
            const path = this.publication.self.replace(/^.*\/api/, '');
            this.router.navigate([path],{ queryParams: { origen: 'publications' } });
        }
    }
	
	getBaseUrl() {
		return `${environment.production? environment.productionUrl  : environment.developmentUrl}`;
	}
	
	getBookImage(): string {
		return	this.publication.book?.images?.length ? this.getBaseUrl() + this.publication.book.images[0] :
				this.publication.book?.bookModel?.cover ? this.getBaseUrl() + this.publication.book.bookModel.cover :
				this.defaultImage;
	}

}