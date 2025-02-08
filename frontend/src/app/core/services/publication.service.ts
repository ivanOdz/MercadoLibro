import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {Publication} from "../models/publication.model";

@Injectable({ providedIn: 'root' })
export class PublicationService {
    baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) {}

    getPublication(publicationUrl: string) : Observable<Publication> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.publications.v1+json'});

        return this.http.get<any>(`${this.baseUrl}${publicationUrl}`, {headers}).pipe(
            tap((r) => console.log("Respuesta de la API con publications:", r))
        );
    }

    getLocation(locationUrl: string) : Observable<Location[]> {
        return this.http.get<any>(`${this.baseUrl}${locationUrl}`).pipe(
            tap((r) => console.log("Respuesta de la API:", r))
        );
    }
}