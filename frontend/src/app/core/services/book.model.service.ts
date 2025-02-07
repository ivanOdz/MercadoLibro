import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Publication} from "../models/publication.model";
import {BookModel} from "../models/bookModel.model";

@Injectable({ providedIn: 'root' })
export class BookModelService {
    baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) {}

    getBookModel(bookModelUrl: string) : Observable<BookModel> {
        return this.http.get<any>(`${this.baseUrl}${bookModelUrl}`);
    }
}