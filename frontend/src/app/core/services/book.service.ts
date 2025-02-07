import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Publication} from "../models/publication.model";
import {Book} from "../models/book.model";

@Injectable({ providedIn: 'root' })
export class BookService {
    baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) {}

    getBook(bookUrn: string) : Observable<Book>  {
        return this.http.get<any>(`${this.baseUrl}${bookUrn}`);
    }
}
