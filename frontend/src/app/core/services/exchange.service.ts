import {Exchange} from "../models/exchange.model";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {map} from "rxjs/operators";
import {catchError, forkJoin, Observable, of, switchMap, throwError} from "rxjs";
import {Injectable} from "@angular/core";
import {Pagination} from "../models/pagination";
import {Message} from "../models/message.model";
import {PublicationService} from "./publication.service";
import {UserService} from "./user.service";
import {BookService} from "./book.service";
import {BookModelService} from "./bookmodel.service";
import {SnackbarService} from "./snackbar.service";

@Injectable({ providedIn: 'root' })
export class ExchangeService {

    constructor(private http: HttpClient, private ps: PublicationService, private us: UserService, private bs: BookService,
                private bms: BookModelService, private snackBarService: SnackbarService) { }

    getExchange(exchangeUrl: string | undefined): Observable<Exchange> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.exchanges.v1+json' });
        return this.http.get<any>(`${exchangeUrl}`, { headers }).pipe(
            catchError((error) => {
                return throwError(() => new Error(error));
            })
        );
    }

    getMessages(messagesUrl: string): Observable<Message[]> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.message.v1+json' });
        return this.http.get<any>(`${messagesUrl}`, { headers }).pipe(
            catchError((error) => {
                return throwError(() => new Error(error));
            })
        );
    }

    getMessage(messageUrl: string | null): Observable<Message> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.message.v1+json' });
        return this.http.get<any>(`${messageUrl}`, { headers }).pipe(
            catchError((error) => {
                return throwError(() => new Error(error));
            })
        );
    }

    private getExchanges(exchangesUrl: string, page: number, state: string,  is_offerer: boolean, is_requester: boolean): Observable< {exchange: Exchange[], pagination: Pagination}> {
        console.log("URL final:", `${exchangesUrl}`);
        let params = new HttpParams()
            .set('state', state)
            .set('is_offerer', is_offerer ? 'true' : 'false')
            .set('is_requester', is_requester ? 'true' : 'false')
            .set('page', page.toString());

        return this.getExchangesByUrl(`${exchangesUrl}&${params.toString()}`);
    }

    getExchangesByUrl(exchangesUrl: string): Observable<{ exchange: Exchange[], pagination: Pagination }> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.exchanges.v1+json' });

        return this.http.get<any>(`${exchangesUrl}`, { headers, observe: 'response' }).pipe(
            map(response => {
                const linkHeader = response.headers.get('link');
                let pagination = new Pagination(linkHeader);
                const exchanges: Exchange[] = response.body.map((exchange: any) => new Exchange(exchange));
                return { exchange: exchanges, pagination: pagination };
            }),
            catchError(error => {
                return of({ exchange: [], pagination: new Pagination(null) }); // Devuelve vacío en caso de error
            })
        );
    }


    //  /exchanges?user_id=123546789&state=accepted&isRequester=true&isOfferer=true&page=1
    //  exchangesUrl: /exchanges?user_id=123546789
    getActiveExchanges(exchangesUrl: string, page: number): Observable< {exchange: Exchange[], pagination: Pagination}> {
        return this.getExchanges(exchangesUrl, page, 'ACCEPTED', true, true);
    }

    // /exchanges?user_id=12345678&state=pending&is_offerer=false&is_requester=true
    getSolicitedExchanges(exchangesUrl: string, page:number): Observable< {exchange: Exchange[], pagination: Pagination}> {
        return this.getExchanges(exchangesUrl, page, 'PENDING', false, true);
    }

    // /exchanges?user_id=12345678state=pending&is_offerer=true&is_requester=false
    getExchangesOffers(exchangesUrl: string, page: number): Observable< {exchange: Exchange[], pagination: Pagination}> {
        return this.getExchanges(exchangesUrl, page, 'PENDING', true, false);
    }


    // /exchanges?user_id=12345678&state=completed&is_offerer=true&is_requester=true&page=1
    getCompletedExchanges(exchangesUrl: string, page: number): Observable< {exchange: Exchange[], pagination: Pagination}> {
        return this.getExchanges(exchangesUrl, page, 'TERMINATED', true, true);
    }

    // /exchanges?user_id=12345678&state=rejected&isOfferer=true&isRequester=true&page=1
    getRejectedExchanges(exchangesUrl: string, page:number):Observable< {exchange: Exchange[], pagination: Pagination}>{
        return this.getExchanges(exchangesUrl, page, 'REJECTED', true, true);
    }


    private updateExchange(exchangeUrl: string, acceptCode: number, requester: boolean | null, accepted: boolean | null): Observable<any> {
        const headers = new HttpHeaders({ 'Content-Type': 'application/vnd.exchanges.update.v1+json' });
        const body: any = {
            acceptCode: acceptCode,
            requester: requester,
            accepted: accepted
        };
        return this.http.patch<void>(`${exchangeUrl}`, body, { headers }).pipe(
            catchError((error) => {
                this.snackBarService.showError('ERROR.UPDATE_EXCHANGE');
                return throwError(() => new Error(error));
            })
        );
    }

    confirmExchange(exchangeUrl: string, acceptCode: number, requester: boolean): Observable<any> {
        return this.updateExchange(exchangeUrl, acceptCode, requester, null);
    }


    acceptExchange(exchangeUrl: string, acceptCode: number, requester: null | boolean): Observable<any> {
        return this.updateExchange(exchangeUrl, acceptCode, requester, true);
    }

    rejectExchange(exchangeUrl: string, acceptCode: number, requester: null | boolean): Observable<any> {
        return this.updateExchange(exchangeUrl, acceptCode, requester, false);
    }


    sendMessage(chatUrl: string | undefined, userUrl: string | undefined, newMessage: any) {
        const headers = new HttpHeaders({
            'Content-Type': 'application/vnd.message.v1+json'
        });

        const body: any = {
            message: newMessage,
            user: userUrl,
            time: null,
            self: null,
            exchange: null
        };

        return this.http.post<void>(`${chatUrl}`, body, { headers, observe: 'response' }).pipe(
            map(response => response.headers.get('Location')),
            catchError((error) => {
                this.snackBarService.showError('ERROR.UPDATE_EXCHANGE');
                return throwError(() => error);
            })
        );
    }

    createExchange(exchangeUrn: string, bookUrn: string | null | undefined, locationUrn: string, publicationUrn: string | null | undefined){
        const headers = new HttpHeaders({
            'Content-Type': 'application/vnd.exchanges.create.v1+json'
        });

        let body = {
            bookURN: bookUrn,
            locationURN: locationUrn,
            publicationURN: publicationUrn
        };


        return this.http.post<void>(`${exchangeUrn}`, body, { headers, observe: 'response' }).pipe(
            map(response => response.headers.get('Location')),
            catchError((error) => {
                this.snackBarService.showError('ERROR.ADD_EXCHANGE');
                return throwError(() => error);
            }
        ));
    }

    processExchanges(exchanges: Exchange[]): Observable<any[]> {
        if (exchanges.length === 0) return of([]);

        const exchangeRequests = exchanges.map((exchange) =>
            forkJoin({
                offererPub: this.ps.getPublication(exchange.offerer),
                requesterPub: this.ps.getPublication(exchange.requester),
            }).pipe(
                switchMap(({ offererPub, requesterPub }) => {
                    if (!offererPub || !requesterPub) return of(null);

                    return forkJoin({
                        offererUser: this.us.getUser(offererPub.user).pipe(catchError(() => of(null))),
                        requesterUser: this.us.getUser(requesterPub.user).pipe(catchError(() => of(null))),
                        offererBook: this.bs.getBook(offererPub.book).pipe(catchError(() => of(null))),
                        requesterBook: this.bs.getBook(requesterPub.book).pipe(catchError(() => of(null))),
                        offererLocations: this.us.getLocationsInPublication(offererPub.locations).pipe(catchError(() => of([]))),
                        requesterLocations: this.us.getLocationsInPublication(requesterPub.locations).pipe(catchError(() => of([]))),
                    }).pipe(
                        switchMap(({ offererUser, requesterUser, offererBook, requesterBook, offererLocations, requesterLocations }) => {
                            if (!offererBook || !requesterBook) return of(null);

                            return forkJoin({
                                offererBookModel: this.bms.getBookModel(offererBook.bookModel).pipe(catchError(() => of(null))),
                                requesterBookModel: this.bms.getBookModel(requesterBook.bookModel).pipe(catchError(() => of(null))),
                                messages: this.getMessages(exchange.chat).pipe(
                                    catchError(() => of([]))
                                )
                            }).pipe(
                                map(({ offererBookModel, requesterBookModel, messages }) => ({
                                    exchange,
                                    offeredPub: {
                                        book: {
                                            state: offererBook.state,
                                            available: offererBook.available,
                                            owner: offererUser,
                                            bookModel: offererBookModel,
                                            images: offererBook.images,
                                            self: offererBook.self
                                        },
                                        locations: offererLocations,
                                        user: offererUser,
                                        publicationState: offererPub.publicationState,
                                        publicationDatetime: new Date(offererPub.publicationDatetime),
                                        favoriteEndpoint: offererPub.favoriteEndpoint,
                                        favoritePublication: null,
                                        isFavoriteTemplate: offererPub.isFavoriteTemplate,
                                        self: offererPub.self,
                                    },
                                    requestedPub: {
                                        book: {
                                            state: requesterBook.state,
                                            available: requesterBook.available,
                                            owner: requesterUser,
                                            bookModel: requesterBookModel,
                                            images: requesterBook.images,
                                            self: requesterBook.self
                                        },
                                        locations: requesterLocations,
                                        user: requesterUser,
                                        publicationState: requesterPub.publicationState,
                                        publicationDatetime: new Date(requesterPub.publicationDatetime),
                                        favoriteEndpoint: requesterPub.favoriteEndpoint,
                                        favoritePublication: null,
                                        isFavoriteTemplate: requesterPub.isFavoriteTemplate,
                                        self: requesterPub.self,
                                    },
                                    messages: messages
                                }))
                            );
                        })
                    );
                })
            )
        );

        return forkJoin(exchangeRequests).pipe(
            map((result) => result.filter((item) => item !== null)) // Eliminamos los nulos
        );
    }
}