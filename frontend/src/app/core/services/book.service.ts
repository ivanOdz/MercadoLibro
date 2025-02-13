import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import {catchError, forkJoin, Observable, switchMap, tap, throwError} from "rxjs";
import {Publication} from "../models/publication.model";
import {Book} from "../models/book.model";
import {BookData2} from "../models/types";
import {BookModelService} from "./bookmodel.service";
import {map} from "rxjs/operators";
import {environment} from "../../../environments/environment";
import {Pagination} from "../models/pagination";
import {Exchange} from "../models/exchange.model";
import {Review} from "../models/review.model";

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

    getBooks({booksUrl, state, genre, search }: { booksUrl: string;state: string; genre: string; search: string}): Observable<{books: Book[], pagination: Pagination, headers: HttpHeaders}> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.books.v1+json' });

        let params = new HttpParams()
            .set('state', state)
            .set('search', search)
            .set('genre', genre);

        return this.http.get<Book[]>(`${booksUrl}`, { headers, params, observe: 'response' }).pipe(
            map(response =>{
                const linkHeader = response.headers.get('Link');
                let pagination = new Pagination(linkHeader);
                const books: Book[] = response.body?.map((book: any) => new Book(book)) || [];
                return { books, pagination, headers: response.headers };
            }
        ));
    }

    /*
    getMyBooks({ booksUrl, state, genre, search }: { booksUrl: string; state: string; genre: string; search: string}): Observable<HttpResponse<BookData2[]>> {
        return this.getBooks( { state, genre, page, search, user: userId! }).pipe(
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
*/

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
