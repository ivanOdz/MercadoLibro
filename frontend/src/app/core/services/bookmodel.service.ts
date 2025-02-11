import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { BookModel } from "../models/bookModel.model";

@Injectable({ providedIn: 'root' })
export class BookModelService {

    constructor(private http: HttpClient) {}

    getBookModel(bookModelUrl: string) : Observable<BookModel> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.book_models.v1+json' });
        return this.http.get<any>(`${bookModelUrl}`, {headers}).pipe(
            //tap((r) => console.log("API response (Get) of Book Model:", r))
        );
    }

	uploadBookModel(bookModelUrl: string, bookData: BookModel, rating: number): Observable<any> {
		
		const headers = new HttpHeaders({'Content-Type': 'application/vnd.book_models.v1+json'});
		
		return this.http.post(`${bookModelUrl}?rating=${rating}`, bookData, { headers, observe: 'response' }).pipe(
			tap((r) => console.log("API response (Post) of Book Model:", r))
		);
	}
}