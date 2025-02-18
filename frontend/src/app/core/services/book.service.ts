import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {catchError, Observable, of, tap, throwError} from "rxjs";
import {Book} from "../models/book.model";
import {BookData} from "../models/types";
import {map} from "rxjs/operators";
import {Pagination} from "../models/pagination";
import {SnackbarService} from "./snackbar.service";

@Injectable({ providedIn: 'root' })
export class BookService {
    constructor(private http: HttpClient, private snackBarService: SnackbarService) {}

    getBook(bookUrn: string) : Observable<Book>  {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.books.v1+json'});
        return this.http.get<any>(`${bookUrn}`, {headers}).pipe(
            catchError((error) => {
                this.snackBarService.showError('ERROR.GET_BOOK');
                return throwError(() => new Error(error));
            })
        );
    }

    getBooks({booksUrl, state, genre, search, available, sort }: { booksUrl: string; state: string; genre: string; search: string; available: boolean, sort: string}): Observable<{books: Book[], pagination: Pagination, headers: HttpHeaders}> {
        let params = new HttpParams()
            .set('state', state)
            .set('search', search)
            .set('genre', genre)
            .set('available', available ? 'true' : 'false')
            .set('sort', sort);

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
                this.snackBarService.showError('ERROR.GET_BOOKS');
                return of({ books: [], pagination: new Pagination(null), headers: error.headers });
            })
        );
    }

    updateBookstate(book: BookData, newState: string): Observable<void> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.books.v1+json' });
        const body = {  state: newState,
                        available: book.available,
                        owner: book.owner,
                        bookModel: book.bookModel,
                        images: book.images,
                        self: book.self
                    };

        return this.http.patch<void>(`${book.self}`, body, { headers }).pipe(
            tap(() => {
                book.state = newState; // Actualizo el estado del libro localmente para que refleje en la card.
            }),
            catchError((error) => {
                this.snackBarService.showError('ERROR.UPDATE_STATE');
                return throwError(() => new Error(error));
            })
        );
    }

    uploadBook(bookUrl: string, bookModelUrl: string, rating: number, condition: string, user: string | undefined, images: string[] | undefined): Observable<any> {
        const headers = new HttpHeaders({'Content-Type': 'application/vnd.books.input.v1+json'});
        const body = {
            condition: condition.toUpperCase(),
            bookModel: bookModelUrl,
            user: user,
            rating: rating,
            imageURNS: images ?? []
        }

        return this.http.post(`${bookUrl}`, body, { headers, observe: 'response' }).pipe(
            tap((response) => console.log("API response (Post) of Book:", response)),
            catchError((error) => {
                this.snackBarService.showError('ERROR.UPLOAD_BOOK');
                return throwError(() => new Error(error));
            })
        );

    }

}
