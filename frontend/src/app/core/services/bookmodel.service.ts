import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {Publication} from "../models/publication.model";
import {BookModel} from "../models/bookModel.model";

@Injectable({ providedIn: 'root' })
export class BookmodelService {
    baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) {}

    getBookModel(bookModelUrl: string) : Observable<BookModel> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.book_models.v1+json'});
        return this.http.get<any>(`${this.baseUrl}${bookModelUrl}`, {headers}).pipe(
            tap((r) => console.log("Respuesta de la API de book model:", r))
        );
    }
}