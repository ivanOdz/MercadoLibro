import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {catchError, forkJoin, Observable, switchMap, tap, throwError} from "rxjs";
import {Publication} from "../models/publication.model";
import {Book} from "../models/book.model";
import {BookData2} from "../models/types";
import {BookModelService} from "./bookmodel.service";
import {map} from "rxjs/operators";
import {environment} from "../../../environments/environment";

@Injectable({ providedIn: 'root' })
export class BookService {
    baseUrl = environment.production ? environment.productionUrl : environment.developmentUrl;

    constructor(private http: HttpClient, private bookModelService: BookModelService) {}

    getBook(bookUrn: string) : Observable<Book>  {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.books.v1+json'});
        return this.http.get<any>(`${bookUrn}`, {headers}).pipe(
            //tap((r) => console.log("Respuesta de la API con books:", r))
        );
    }

    private getBooks({ state, genre, page, search, user }: { state: string; genre: string; page: number; search: string; user: string | null}): Observable<HttpResponse<Book[]>> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.books.v1+json' });

        let queryParams = '';

        if (search) {
            queryParams += `search=${search}`;
        }

        if (state) {
            if (queryParams) queryParams += '&';
            queryParams += `state=${state}`;
        }

        if (genre) {
            if (queryParams) queryParams += '&';
            queryParams += `genre=${genre}`;
        }

        if(user){
            if (queryParams) queryParams += '&';

            queryParams += `owner=${user}`;
        }

        if (page !== undefined && page !== null) {
            if (queryParams) queryParams += '&';
            queryParams += `page=${page}`;
        }

        const url = `${this.baseUrl}/books${queryParams ? '?' + queryParams : ''}`;

        return this.http.get<Book[]>(url, {
            headers,
            observe: 'response'
        }).pipe(
            //tap((response) => console.log("Respuesta completa de la API:", response))
        );
    }

    getMyBooks({ state, genre, page, search, user }: { state: string; genre: string; page: number; search: string; user: string }): Observable<HttpResponse<BookData2[]>> {
        const userId = user.split("/").pop();
        return this.getBooks({ state, genre, page, search, user: userId! }).pipe(
            switchMap((response) => {
                const books = response.body || [];

                const detailsRequests = books.map((book) =>
                    this.bookModelService.getBookModel(book.bookModel).pipe(
                        map((bookModel) => ({
                            ...book,
                            bookModel
                        }))
                    )
                );

                return forkJoin(detailsRequests).pipe(
                    map((updatedBooks) =>
                        new HttpResponse({ body: updatedBooks, headers: response.headers }))
                );
            })
        );
    }


    updateBookstate(book: BookData2, newState: string): Observable<void> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.books.v1+json' });
        const body = { state: newState };

        return this.http.patch<void>(`${book.self}`, body, { headers }).pipe(
            tap(() => {
                book.state = body.state; // Actualizo el estado del libro localmente para que refleje en la card.
                //console.log('Libro actualizado localmente:', book);
            }),
            catchError((error) => {
                if (error.status === 404) {
                    console.log("Haciendo redirección a /404");
                }
                return throwError(() => new Error(error));
            })
        );
}

}
