import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { User } from "../models/user.model";
import { map } from "rxjs/operators";
import { catchError, EMPTY, Observable, tap, throwError } from "rxjs";
import { Location } from "../models/location.model";
import {Review} from "../models/review.model";

@Injectable({ providedIn: 'root' })

export class UserService {
    baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) {

    }

    getUser(userUrl: string): Observable<User> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.users.v1+json'});

        // userUrl = '/users/{id}'
        return this.http.get<any>(`${this.baseUrl}/${userUrl}`, { headers }).pipe(
            map((userData) => {
                return new User(userData);
            })
        );
    }

    registerUser(mail: string, username: string, password: string): Observable<string | null> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.users.v1+json' });

        // Retornamos el Observable y no hacemos la suscripción aquí
        return this.http.post<any>(`${this.baseUrl}/users`, { username, mail, password }, {
            headers,
            observe: 'response'
        }).pipe(
            // Retornamos la URL del header Location
            map(response => response.headers.get('Location'))
        );
    }

    verifyUser(verificationCode: number): Observable<any> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.verification.code.v1+json' });

        return this.http.post<any>(`${this.baseUrl}/users`, {verificationCode} , { headers, observe: 'response' }).pipe(
            tap((response) => {
                if (response.status === 204) {
                    console.log('Usuario verificado exitosamente');
                }
            }),
            catchError((error) => {
                console.error('Error en la verificación del usuario', error);
                return throwError(() => error);
            })
        );
    }

    changePasswordRequest(email: string): Observable<string | null> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.users.email.v1+json' });

        return this.http.post<any>(`${this.baseUrl}/users`, { email }, { headers }).pipe(
            catchError(error => {
                if (error.status === 404) {
                    return EMPTY;
                }
                return throwError(() => error);
            })
        );
    }

    changePassword(passwordToken: number | undefined, newPassword: string): Observable<void> {
        const headers = new HttpHeaders({'Content-Type': 'application/vnd.users.password.v1+json'});

        return this.http.patch<void>(`${this.baseUrl}/users/${passwordToken}`, {newPassword}, {headers}).pipe(
            catchError(error => {
                if (error.status === 404) {
                    return EMPTY;
                }
                return throwError(() => error);
            })
        );
    }

    updateUsername(user: User, newUsername: string): Observable<void> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.users.v1+json' });

        const body = {
            newUsername: newUsername,
            language: null
        };

        console.log(body);

        return this.http.patch<void>(`${this.baseUrl}${user.self}`, body, { headers });
    }

    updateLanguage(user: User, language: string): Observable<void> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.users.v1+json' });

        const body = {
            newUsername: null,
            language: language
        };

        console.log(body);

        return this.http.patch<void>(`${this.baseUrl}${user.self}`, body, { headers });
    }

    getLocations(user: User): Observable<Location[]> {
        return this.http.get<Location[]>(`${this.baseUrl}${user.locations}`).pipe(
            map((locationsData: any[]) => locationsData.map(location => new Location(location)))
        );
    }

    addLocation(user: User, location: string) {
        return this.http.post<void>(`${this.baseUrl}${user.locations}`, { location });
    }

    removeLocation(user: User, location: Location) {
        return this.http.delete<void>(`${this.baseUrl}${location.self}`);
    }

    getReviews(user: User): Observable<Review[]> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.reviews.v1+json' });

        return this.http.get<Review[]>(`${this.baseUrl}${user.reviews}`, { headers }).pipe(
            map((reviewsData: any[]) => reviewsData.map(review => new Review(review)))
        );
    }


}
