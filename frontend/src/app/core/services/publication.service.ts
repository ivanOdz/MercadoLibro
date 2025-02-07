import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Publication} from "../models/publication.model";

@Injectable({ providedIn: 'root' })
export class PublicationService {
    baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) {}

    getPublication(publicationUrl: string) : Observable<Publication> {
        return this.http.get<any>(`${this.baseUrl}${publicationUrl}`);
    }

    getLocation(locationUrl: string) : Observable<Location[]> {
        return this.http.get<any>(`${this.baseUrl}${locationUrl}`);
    }
}