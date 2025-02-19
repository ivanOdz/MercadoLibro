import { Injectable } from "@angular/core";
import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { User } from "../models/user.model";
import { map } from "rxjs/operators";
import { catchError, EMPTY, Observable, tap, throwError } from "rxjs";
import { Location } from "../models/location.model";
import { Review } from "../models/review.model";
import { TranslateService } from "@ngx-translate/core";
import { Pagination } from "../models/pagination";
import { environment } from "../../../environments/environment";

@Injectable({ providedIn: 'root' })
export class UserService {
    baseUrl = environment.production ? environment.productionUrl : environment.developmentUrl;

    constructor(private http: HttpClient, private translate: TranslateService) {

    }

    getUser(userUrl: string | undefined): Observable<User> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.users.v1+json'});

        // userUrl = '/users/{id}'
        return this.http.get<any>(`${userUrl}`, { headers }).pipe(
//           tap((userData) => console.log('Respuesta de la API de user:', userData)),
		   
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
            map(response => response.headers.get('Location')),
            catchError((error) => {
                return throwError(() => error);
            })
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
            language: null,
            newUsername: newUsername
        };

        console.log(body);

        return this.http.patch<void>(`${user.self}`, body, { headers }).pipe(
            catchError((error: HttpErrorResponse) => {
                if (error.status === 409) {
                    const errorMessage = this.translate.instant("PROFILE.USERNAME_ALREADY_EXISTS");
                    return throwError(() => new Error(errorMessage));
                }
                const genericErrorMessage = this.translate.instant("PROFILE.UPDATE_ERROR");
                return throwError(() => new Error(genericErrorMessage));
            })
        );
    }

    updateLanguage(user: User | null, language: string): Observable<void> {
        if (!user) {
            return EMPTY;
        }

        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.users.v1+json' });

        const body = {
            newUsername: null,
            language: language
        };

        console.log(body);

        return this.http.patch<void>(`${user.self}`, body, { headers });
    }

    getLocations(user: User): Observable<Location[]> {
        return this.http.get<Location[]>(`${user.locations}`).pipe(
            map((locationsData: any[]) => locationsData.map(location => new Location(location)))
        );
    }

    addLocation(user: User, location: string) {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.location.v1+json', 'Accept': 'application/vnd.location.v1+json' });

        return this.http.post<any>(`${user.locations}`, { location }, { headers, observe: 'response' }).pipe(
            tap((response) => {
                if (response.status === 204) {
                    console.log('Ubicación creada exitosamente');
                }
            }),
            catchError((error) => {
                console.error('Error en la creación de la ubicación', error);
                return throwError(() => error);
            })

        );
    }

    removeLocation(location: Location) {
        return this.http.delete<void>(`${location.self}`);
    }

    getReviews(user: User, page: number): Observable<{ reviews: Review[], pagination: Pagination }> {
        const url = `${user.reviews}?page=${page}`;
        return this.getReviewsFromUrl(url);
    }

    getReviewsFromUrl(url: string): Observable<{ reviews: Review[], pagination: Pagination }> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.user.review.v1+json' });

        return this.http.get<Review[]>(url, { headers, observe: 'response' }).pipe(
            map(response => {
                const linkHeader = response.headers.get('Link');
                let pagination = new Pagination(linkHeader);
                const reviews: Review[] = response.body?.map((review: any) => new Review(review)) || [];
                return { reviews, pagination };
            })
        );
    }


    getLocationsInPublication(publicationLocationUrl: string | undefined): Observable<Location[]> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.location.v1+json' });

        return this.http.get<any[]>(`${publicationLocationUrl}`, { headers }).pipe(
            map((l) => l.map((l: any) => new Location(l))));
    }


    postReview(reviewsUrn: string | undefined, exchangesUrn: string | undefined, rating: number, description: string): Observable<void> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.user.review.v1+json' });

        const body: any = {
            description: description,
            reviewDate: null,
            rating: rating,
            self: null,
            subject: null,
            reviewer: null,
            exchange: exchangesUrn,
        }

        return this.http.post<void>(`${reviewsUrn}`, body ,{ headers });
    }

}
