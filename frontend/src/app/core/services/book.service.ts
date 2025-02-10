import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {Publication} from "../models/publication.model";
import {Book} from "../models/book.model";

@Injectable({ providedIn: 'root' })
export class BookService {

    constructor(private http: HttpClient) {}

    getBook(bookUrn: string) : Observable<Book>  {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.books.v1+json'});
        return this.http.get<any>(`${bookUrn}`, {headers}).pipe(
            //tap((r) => console.log("Respuesta de la API con books:", r))
        );
    }
}
