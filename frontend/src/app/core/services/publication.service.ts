import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {catchError, forkJoin, Observable, of, switchMap, tap, throwError} from "rxjs";
import {Publication} from "../models/publication.model";
import {BookData2, FavoritePublication, PublicationData, PublicationData2} from "../models/types";
import {map} from "rxjs/operators";
import {BookService} from "./book.service";
import {UserService} from "./user.service";
import {BookModel} from "../models/bookModel.model";
import {BookmodelService} from "./bookmodel.service";
import {AuthService} from "./auth.service";

@Injectable({ providedIn: 'root' })
export class PublicationService {
    baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient, private authService: AuthService, private bookService: BookService, private userService: UserService, private bookModelService: BookmodelService) {}

    getPublications({ state, genre, page, search }: { state: string; genre: string; page: number; search: string }): Observable<HttpResponse<Publication[]>> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.publications.v1+json' });

        let queryParams = '';

        if (search) {
            queryParams += `search=${search}`;
        }

        if (state) {
            if (queryParams) queryParams += '&';
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

        return this.http.get<Publication[]>(url, {
            headers,
            observe: 'response'
        }).pipe(
            //tap((response) => console.log("Respuesta completa de la API:", response))
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

    getFavoritePublication(favoriteUrl: string, userUrl: string): Observable<FavoritePublication | null> {
        const userId = userUrl.split("/").pop();
        return this.http.get<FavoritePublication>(`${favoriteUrl.replace("{user_id}", <string>userId)}`).pipe(
            tap((r) => console.log("Respuesta de la API:", r)),
            catchError((error) => {
                if (error.status === 404) {
                    return of(null);
                }
                return throwError(() => error);
            })
        );
    }

    getPublicationsWithDetails({
                                   state,
                                   genre,
                                   page,
                                   search
                               }: {
        state: string;
        genre: string;
        page: number;
        search: string;
    }): Observable<HttpResponse<PublicationData2[]>> {
        return this.getPublications({state, genre, page, search}).pipe(
            switchMap((response) => {
                const publications = response.body || [];

                const detailsRequests = publications.map((publication) =>
                    forkJoin({
                        book: this.bookService.getBook(publication.book),
                        locations: this.userService.getLocationsInPublication(publication.locations),
                        user: this.userService.getUser(publication.user),
                    }).pipe(
                        switchMap(({book, locations, user}) =>
                            forkJoin({
                                bookModel: this.bookModelService.getBookModel(book.bookModel)
                            }).pipe(
                                map(({bookModel}) => ({
                                    book: {...book, bookModel},
                                    locations,
                                    user,
                                    publicationState: publication.publicationState,
                                    publicationDatetime: publication.publicationDatetime,
                                    favoriteEndpoint: publication.favoriteEndpoint,
                                    self: publication.self,
                                    favoritePublication: null,
                                    isFavoriteTemplate: publication.isFavoriteTemplate
                                }))
                            )
                        )
                    )
                );
                return forkJoin(detailsRequests).pipe(
                    map((publicationData: PublicationData2[]) =>
                        new HttpResponse<PublicationData2[]>({
                            body: publicationData,
                            headers: response.headers
                        })
                    )
                );
            })
        );
    }

    setFavoritePublication(userUrl: string, publications: PublicationData2[]): Observable<any> {
        if (!userUrl || publications.length === 0) {
            return of();
        }

        const favoriteRequests = publications.map((publication) =>
            this.getFavoritePublication(publication.isFavoriteTemplate, userUrl).pipe(
                tap((favorite) => publication.favoritePublication = favorite)
            )
        );

        return forkJoin(favoriteRequests).pipe(
            tap(() => console.log("Se han actualizado las publicaciones con su estado de favorito"))
        );
    }
}