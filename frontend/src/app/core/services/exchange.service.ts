import {Exchange} from "../models/exchange.model";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {map} from "rxjs/operators";
import {catchError, Observable, throwError} from "rxjs";
import {Injectable} from "@angular/core";
import {Pagination} from "../models/pagination";
import {AuthService} from "./auth.service";
import {Message} from "../models/message.model";

@Injectable({ providedIn: 'root' })
export class ExchangeService {

    constructor(private http: HttpClient) { }


    getMessages(messagesUrl: string): Observable<Message[]> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.message.v1+json' });
        return this.http.get<any>(`${messagesUrl}`, { headers });
    }

    getMessage(messageUrl: string | null): Observable<Message> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.message.v1+json' });
        return this.http.get<any>(`${messageUrl}`, { headers });
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

    getExchangesByUrl(exchangesUrl: string): Observable< {exchange: Exchange[], pagination: Pagination}> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.exchanges.v1+json' });

        return this.http.get<any>(`${exchangesUrl}`, { headers, observe: 'response' }).pipe(
            map(response => {
                const linkHeader = response.headers.get('link');
                let pagination = new Pagination(linkHeader);
                console.log("Paginación de exchanges:", pagination);
                const exchanges: Exchange[] = response.body.map((exchange: any) => new Exchange(exchange));
                return {exchange: exchanges, pagination: pagination };
            }));
    }

    //  /exchanges?user_id=123546789&state=accepted&isRequester=true&isOfferer=true&page=1
    //  exchangesUrl: /exchanges?user_id=123546789
    getActiveExchanges(exchangesUrl: string, page: number): Observable< {exchange: Exchange[], pagination: Pagination}> {
        return this.getExchanges(exchangesUrl, page, 'ACCEPTED', true, true);
    }

    // /exchanges?user_id=12345678state=pending&isOfferer=false&isRequester=true
    getSolicitedExchanges(exchangesUrl: string, page:number): Observable< {exchange: Exchange[], pagination: Pagination}> {
        return this.getExchanges(exchangesUrl, page, 'PENDING', false, true);
    }

    // /exchanges?user_id=12345678state=pending&isOfferer=true&isRequester=false
    getExchangesOffers(exchangesUrl: string, page: number): Observable< {exchange: Exchange[], pagination: Pagination}> {
        return this.getExchanges(exchangesUrl, page, 'PENDING', true, false);
    }


    // /exchanges?user_id=12345678&state=completed&isOfferer=true&isRequester=true&page=1
    getCompletedExchanges(exchangesUrl: string, page: number): Observable< {exchange: Exchange[], pagination: Pagination}> {
        return this.getExchanges(exchangesUrl, page, 'TERMINATED', true, true);
    }

    // /exchanges?user_id=12345678&state=rejected&isOfferer=true&isRequester=true&page=1
    getRejectedExchanges(exchangesUrl: string, page:number):Observable< {exchange: Exchange[], pagination: Pagination}>{
        return this.getExchanges(exchangesUrl, page, 'REJECTED', true, true);
    }


    private updateExchange(exchangeUrl: string, acceptCode: number, requester: boolean | null, accepted: boolean | null): Observable<any> {
        const headers = new HttpHeaders({
            'Content-Type': 'application/vnd.exchanges.update.v1+json'
        });

        const body: any = {
            acceptCode: acceptCode,
            requester: requester,
            accepted: accepted
        };

        console.log("URL final:", `${exchangeUrl}`);
        console.log("Body enviado:", body);

        return this.http.patch<void>(`${exchangeUrl}`, body, { headers }).pipe(
            catchError((error) => {
                console.error("Error al actualizar el intercambio:", error);
                return throwError(() => error);
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
                console.error("Error al actualizar el intercambio:", error);
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
                console.error("Error al crear el intercambio:", error);
                return throwError(() => error);
            }
        ));
    }
}