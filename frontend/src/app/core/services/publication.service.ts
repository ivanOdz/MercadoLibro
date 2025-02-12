import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {catchError, firstValueFrom, forkJoin, Observable, of, switchMap, tap, throwError} from "rxjs";
import {Publication} from "../models/publication.model";
import {BookData2, FavoritePublication, PublicationData, PublicationData2} from "../models/types";
import {map} from "rxjs/operators";
import {BookService} from "./book.service";
import {UserService} from "./user.service";
import {BookModel} from "../models/bookModel.model";
import {BookModelService} from "./bookmodel.service";
import {AuthService} from "./auth.service";
import {User} from "../models/user.model";
import {environment} from "../../../environments/environment";

@Injectable({ providedIn: 'root' })
export class PublicationService {
    baseUrl = environment.production ? environment.productionUrl : environment.developmentUrl;

    constructor(private http: HttpClient, private authService: AuthService, private bookService: BookService, private userService: UserService, private bookModelService: BookModelService) {}

    private getPublications({ state, genre, page, search, favorites, user }: { state: string; genre: string; page: number; search: string; favorites: boolean; user: string | null}): Observable<HttpResponse<Publication[]>> {
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

        if(favorites){
            if(queryParams) queryParams += '&';
            queryParams += `favorites=true`;
        }

        if(user){
            if (queryParams) queryParams += '&';
            queryParams += `user_id=${user}`;
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

    getGeneralPublications({state, genre, page, search}: {state: string; genre: string; page: number; search: string;}): Observable<HttpResponse<PublicationData2[]>> {
        return this.getPublicationsWithDetails({state, genre, page, search, favorites: false, user: null});
    }

    getMyPublications({state, genre, page, search, user}: {state: string; genre: string; page: number; search: string; user: string}): Observable<HttpResponse<PublicationData2[]>> {
        const userId = user.split("/").pop();
        return this.getPublicationsWithDetails({state, genre, page, search, favorites: false, user: userId!});
    }

    getFavoritePublications({state, genre, page, search, user}: {state: string; genre: string; page: number; search: string; user: string}): Observable<HttpResponse<PublicationData2[]>> {
        const userId = user.split("/").pop();
        return this.getPublicationsWithDetails({state, genre, page, search, favorites: true, user: userId!});
    }

    private getPublicationsWithDetails({state, genre, page, search, favorites, user}: {state: string; genre: string; page: number; search: string; favorites: boolean; user: string | null}): Observable<HttpResponse<PublicationData2[]>> {
        return this.getPublications({state, genre, page, search, favorites, user}).pipe(
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
                    map((publicationData: any[]) => {
                        const transformedData: PublicationData2[] = publicationData.map(data => ({
                            book: {
                                ...data.book,
                                owner: data.user
                            },
                            locations: data.locations,
                            user: data.user,
                            publicationState: data.publicationState,
                            publicationDatetime: data.publicationDatetime,
                            favoriteEndpoint: data.favoriteEndpoint,
                            self: data.self,
                            favoritePublication: null,
                            isFavoriteTemplate: data.isFavoriteTemplate
                        }));
                        console.log("Publicaciones con detalles:", transformedData);
                        return new HttpResponse<PublicationData2[]>({
                            body: transformedData,
                            headers: response.headers
                        });
                    })
                );
            })
        );
    }

    // favoriteEndpoint -> publication.favoriteEndpoint
    likePublication(publication: PublicationData2, userUrl: string): Observable<any> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.users.v1+json'});

        return this.http.post(`${publication.favoriteEndpoint}`, {user_id: userUrl}).pipe(
            tap(() => console.log("Publicación marcada como favorita"))
        );
    }

    unlikePublication(publication: PublicationData2): Observable<any> {
        return this.http.delete<void>(`${publication.favoritePublication?.self}`).pipe(
            tap(() => publication.favoritePublication = null) // Después de eliminar, asigna null
        );
    }

    deleteMyPublication(publicationSelfUrn: string){
        return this.http.delete<void>(publicationSelfUrn);
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