import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, tap} from "rxjs";
import {Publication} from "../models/publication.model";

@Injectable({ providedIn: 'root' })
export class PublicationService {
    baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) {}

    getPublications({ state, genre, page, search }: { state: string; genre: string; page: number; search: string }): Observable<Publication[]> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.publications.v1+json' });

        let queryParams = '';

        if (search) {
            queryParams += `search=${search}`;
        }

        if (state) {
            queryParams += `state=${state}`;
        }

        if (genre) {
            if (queryParams) queryParams += '&';
            queryParams += `genre=${genre}`;
        }
        if (page !== undefined && page !== null) {
            if (queryParams) queryParams += '&';
            queryParams += `page=${page}`;
        }
        const url = `${this.baseUrl}/publications${queryParams ? '?' + queryParams : ''}`;

        return this.http.get<Publication[]>(url, { headers }).pipe(
            tap((r) => console.log("Respuesta de la API con publications:", r))
        );
    }

    getPublication(publicationUrl: string) : Observable<Publication> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.publications.v1+json'});

        return this.http.get<any>(`${publicationUrl}`, {headers}).pipe(
            tap((r) => console.log("Respuesta de la API con publications:", r))
        );
    }

    getLocation(locationUrl: string) : Observable<Location[]> {
        return this.http.get<any>(`${locationUrl}`).pipe(
            tap((r) => console.log("Respuesta de la API:", r))
        );
    }
}