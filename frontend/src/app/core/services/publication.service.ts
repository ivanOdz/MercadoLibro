import {Injectable} from "@angular/core";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, forkJoin, Observable, of, switchMap, tap, throwError} from "rxjs";
import {Publication} from "../models/publication.model";
import {FavoritePublication, ObservablePublication, ObservablePublicationData, PublicationData} from "../models/types";
import {map} from "rxjs/operators";
import {BookService} from "./book.service";
import {UserService} from "./user.service";
import {BookModelService} from "./bookmodel.service";
import {AuthService} from "./auth.service";
import {environment} from "../../../environments/environment";
import {Location} from "../models/location.model";
import {Pagination} from "../models/pagination";
import { User } from "../../core/models/user.model";

@Injectable({ providedIn: 'root' })
export class PublicationService {
    baseUrl = environment.production ? environment.productionUrl : environment.developmentUrl;

    constructor(private http: HttpClient, private authService: AuthService, private bookService: BookService, private userService: UserService, private bookModelService: BookModelService){}

    // api call
    private getPublicationsByUrl(url: string): ObservablePublication {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.publications.v1+json' });

        return this.http.get<any>(url, { headers, observe: 'response' }).pipe(
            map(response => {
                const linkHeader = response.headers.get('link');
                let pagination = new Pagination(linkHeader);
				const totalResults = Number(response.headers.get('X-Total-Count')) || 0;
                const publications: Publication[] = response.body.map((publication: any) => new Publication(publication));
                return { publications: publications, pagination: pagination, headers: this.processHeaders(response.headers), totalResults: totalResults };
            }),
            catchError(error => {
                return of({ publications: [], pagination: new Pagination(null), headers: { conditionHeaders: {}, genreHeaders: {} }, totalResults: 0 });
            })
        );
    }

    private processParams(state: string, genre: string, page: number, search: string, favorites: boolean | null, user: string | null): string {
        let queryParams = '';

        if (search) queryParams += `search=${search}`;

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

        if(user !== null){
            if (queryParams) queryParams += '&';
            queryParams += `user_id=${user}`;
        }

        if (page !== undefined && page !== null) {
            if (queryParams) queryParams += '&';
            queryParams += `page=${page}`;
        }

        return queryParams;

    }

    getGeneralPublications(state: string, genre: string, page: number, search: string): ObservablePublicationData {
        let url = `${this.baseUrl}/publications?${this.processParams(state, genre, page, search, false, null)}`;
        return this.getPublicationsWithDetails(url);
    }

    //myPublicationsUrl: {base_path}/publications?user_id={user_id}
    getMyPublications(myPublicationsUrl: string, state: string, genre: string, page: number, search: string): ObservablePublicationData{
        let url = `${myPublicationsUrl}&${this.processParams(state, genre, page, search, false, null)}`;
        return this.getPublicationsWithDetails(url);
    }

    getFavoritePublications(favoritePubsUrl: string, state: string, genre: string, page: number, search: string): ObservablePublicationData {
        let url = `${favoritePubsUrl}&${this.processParams(state, genre, page, search, null, null)}`;
        return this.getPublicationsWithDetails(url);
    }

    getPublicationsWithDetails(publicationEndpointUrl: string): ObservablePublicationData {
        return this.getPublicationsByUrl(publicationEndpointUrl).pipe(
            switchMap((response) => {
                const publications = response.publications;

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
                        const transformedData: PublicationData[] = publicationData.map(data => ({
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
                        return ({
                            publicationData: transformedData,
                            pagination: response.pagination,
                            headers: response.headers,
							totalResults: response.totalResults
                        });
                    })
                );
            })
        );
    }

    private processHeaders(headers: any): {conditionHeaders: Record<string, string>, genreHeaders: Record<string, string>} {
        const newConditionHeaders: Record<string, string> = {};
        const newGenreHeaders: Record<string, string> = {};

        headers.keys().forEach((key: string) => {
            const value = headers.get(key);
            if (value !== null) {
                if (key.startsWith("x-bookstate-")) {
                    newConditionHeaders[key] = value;
                } else if (key.startsWith("x-genre-")) {
                    newGenreHeaders[key] = value;
                }
            }
        });

        return {conditionHeaders: newConditionHeaders, genreHeaders: newGenreHeaders};
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
	
    likePublication(publication: PublicationData, user: User): Observable<any> {
		const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.users.v1+json' });
		
        return this.http.post(`${publication.favoriteEndpoint}`, user, { headers }).pipe(
            tap(() => console.log("Publicación marcada como favorita"))
        );
    }

    unlikePublication(publication: PublicationData): Observable<any> {
		console.log("unlikePublication");
		console.log(publication);
        return this.http.delete<void>(`${publication.favoritePublication?.self}`).pipe(
            tap(() => publication.favoritePublication = null) // Después de eliminar, asigna null
        );
    }

    deleteMyPublication(publicationSelfUrn: string){
        return this.http.delete<void>(publicationSelfUrn);
    }

    setFavoritePublication(userUrl: string, publications: PublicationData[]): Observable<any> {
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

    createPublication(userUrl: string, bookUrl: string | null, selectedLocation: Location | null) {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.publications.input.v1+json' });

        let body = {
            userURN: userUrl,
            bookURN: bookUrl,
            locationURN: selectedLocation?.self
        }

        return this.http.post(`${this.baseUrl}/publications`, body, {headers}).pipe(
            tap((r) => {
                console.log("Publicación creada:", r);
            })
        )
    }
}