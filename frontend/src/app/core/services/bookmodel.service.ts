import { Injectable } from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
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

    uploadBookModel(bookModelUrl: string, bookData: BookModel, cover: string | undefined): Observable<string> {
        console.log('Book MODEL SERVICE:', cover);
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.book_models.v1+json' });
        const body = {
            isbn: bookData.isbn,
            title: bookData.title,
            editorial: bookData.editorial,
            description: bookData.description,
            genre: bookData.genre,
            edition: bookData.edition,
            weight: bookData.weight,
            pages: bookData.pages,
            bookLanguage: bookData.bookLanguage,
            dimension: bookData.dimension,
            publicationYear: bookData.publicationYear,
            pocketEdition: bookData.pocketEdition,
            hardcover: bookData.hardcover,
            ratingCount: bookData.ratingCount,
            averageRating: bookData.averageRating,
            authors: bookData.authors,
            cover: cover,
            self: bookData.self
        }
        console.log()
        return this.http.post(`${bookModelUrl}`, body, { headers, observe: 'response' }).pipe(
            map(response => response.headers.get('Location') || '')
        );
    }


    getBookModels({ bookModelsUrl, genre, search, sort }: { bookModelsUrl: string; genre: string; search: string, sort: string }): Observable<{ bookModels: BookModel[], pagination: Pagination, headers: HttpHeaders }> {
        let params = new HttpParams()
            .set('search', search)
            .set('genre', genre)
            .set('sort', sort);

        return this.getBookModelsByUrl(`${bookModelsUrl}?${params.toString()}`);
    }

    getBookModelsByUrl(bookModelsUrl: string): Observable<{ bookModels: BookModel[], pagination: Pagination, headers: HttpHeaders }> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.book_models.v1+json' });

        return this.http.get<BookModel[]>(`${bookModelsUrl}`, { headers, observe: 'response' }).pipe(
            map(response => {
                const linkHeader = response.headers.get('Link');
                let pagination = new Pagination(linkHeader);
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