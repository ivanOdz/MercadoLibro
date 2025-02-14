import { Injectable } from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {catchError, Observable, of, tap} from "rxjs";
import { BookModel } from "../models/bookModel.model";
import {Pagination} from "../models/pagination";
import {map} from "rxjs/operators";

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

    getBookModels({ bookModelsUrl, genre, search }: { bookModelsUrl: string; genre: string; search: string }): Observable<{ bookModels: BookModel[], pagination: Pagination, headers: HttpHeaders }> {
        let params = new HttpParams()
            .set('search', search)
            .set('genre', genre);

        return this.getBookModelsByUrl(`${bookModelsUrl}?${params.toString()}`);
    }

    getBookModelsByUrl(bookModelsUrl: string): Observable<{ bookModels: BookModel[], pagination: Pagination, headers: HttpHeaders }> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.book_models.v1+json' });

        return this.http.get<BookModel[]>(`${bookModelsUrl}`, { headers, observe: 'response' }).pipe(
            map(response => {
                const linkHeader = response.headers.get('Link');
                let pagination = new Pagination(linkHeader);
                console.log("Paginación de book models:", pagination);
                const bookModels: BookModel[] = response.body?.map((bm: any) => new BookModel(bm)) || [];
                return { bookModels, pagination, headers: response.headers };
            }),
            catchError(error => {
                console.error("Error al obtener los modelos de libros:", error);
                return of({ bookModels: [], pagination: new Pagination(null), headers: error.headers });
            })
        );
    }

}