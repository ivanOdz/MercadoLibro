import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../models/user.model";
import {map} from "rxjs/operators";
import {catchError, EMPTY, Observable, tap, throwError} from "rxjs";

@Injectable({ providedIn: 'root' })

export class UserService {


    constructor(private http: HttpClient) { }

    baseUrl = 'http://localhost:8080/api';

    getUser(userUrl: string): Observable<User> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.users.v1+json'});

        // userUrl = '/users/{id}'
        return this.http.get<any>(`${this.baseUrl}${userUrl}`, { headers }).pipe(
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

    changePasswordRequest(mail: string): Observable<string | null> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.users.email.v1+json' });

        return this.http.post<any>(`${this.baseUrl}/users`, { mail }, { headers }).pipe(
            catchError(error => {
                if (error.status === 404) {
                    return EMPTY;
                }
                return throwError(() => error);
            })
        );
    }

    changePassword(passwordToken: number, newPassword: string): Observable<void> {
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
}
