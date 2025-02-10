import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {forkJoin, Observable, switchMap, tap} from "rxjs";
import {Publication} from "../models/publication.model";
import {Book} from "../models/book.model";
import {BookData2} from "../models/types";
import {BookModelService} from "./bookmodel.service";
import {map} from "rxjs/operators";

@Injectable({ providedIn: 'root' })
export class BookService {
    baseUrl = 'http://localhost:8080/api';

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

}
