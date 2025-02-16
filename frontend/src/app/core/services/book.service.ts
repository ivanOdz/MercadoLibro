import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {catchError, Observable, of, tap, throwError} from "rxjs";
import {Book} from "../models/book.model";
import {BookData} from "../models/types";
import {map} from "rxjs/operators";
import {Pagination} from "../models/pagination";

@Injectable({ providedIn: 'root' })
export class BookService {
    constructor(private http: HttpClient) {}

    getBook(bookUrn: string) : Observable<Book>  {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.books.v1+json'});
        return this.http.get<any>(`${bookUrn}`, {headers}).pipe(
            //tap((r) => console.log("Respuesta de la API con books:", r))
        );
    }

    getBooks({booksUrl, state, genre, search, available }: { booksUrl: string;state: string; genre: string; search: string; available: boolean}): Observable<{books: Book[], pagination: Pagination, headers: HttpHeaders}> {
        let params = new HttpParams()
            .set('state', state)
            .set('search', search)
            .set('genre', genre)
            .set('available', available ? 'true' : 'false');

        return this.getBooksByUrl(`${booksUrl}&${params.toString()}`);
    }

    getBooksByUrl(booksUrl: string): Observable<{ books: Book[], pagination: Pagination, headers: HttpHeaders }> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.books.v1+json' });

        return this.http.get<Book[]>(`${booksUrl}`, { headers, observe: 'response' }).pipe(
            map(response => {
                const linkHeader = response.headers.get('Link');
                let pagination = new Pagination(linkHeader);
                console.log("Paginación de books:", pagination);
                const books: Book[] = response.body?.map((book: any) => new Book(book)) || [];
                console.log("Libros obtenidos:", books);
                return { books: books, pagination: pagination, headers: response.headers };
            }),
            catchError(error => {
                console.error("Error al obtener los libros:", error);
                return of({ books: [], pagination: new Pagination(null), headers: error.headers });
            })
        );
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

    updateBookstate(book: BookData, newState: string): Observable<void> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.books.v1+json' });
        const body = { state: newState };

        return this.http.patch<void>(`${book.self}`, body, { headers }).pipe(
            tap(() => {
                console.log("Libro uri:", book.self);
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

    uploadBook(bookUrl: string, bookModelUrl: string, rating: number, condition: string, user: string | undefined): Observable<any> {
        const headers = new HttpHeaders({'Content-Type': 'application/vnd.books.input.v1+json'});
        const body = {
            condition: condition.toUpperCase(),
            bookModel: bookModelUrl,
            user: user,
            rating: rating,
            imageURNS:
                ['http://localhost:8080/api/images/84',
                'http://localhost:8080/api/images/85'
                ]
        };

        return this.http.post(`${bookUrl}`, body, { headers, observe: 'response' }).pipe(
            tap((response) => console.log("API response (Post) of Book:", response))
        );

    }

}
