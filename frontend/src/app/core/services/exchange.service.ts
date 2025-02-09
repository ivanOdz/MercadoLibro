import {Exchange} from "../models/exchange.model";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {map} from "rxjs/operators";
import {catchError, Observable, throwError} from "rxjs";
import {Injectable} from "@angular/core";
import {Pagination} from "../models/pagination";

@Injectable({ providedIn: 'root' })
export class ExchangeService {
    states = ['ACCEPTED', 'PENDING', 'COMPLETED', 'REJECTED'];

    constructor(private http: HttpClient) { }


    getMessages(messagesUrl: string): Observable<any> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.message.v1+json' });
        return this.http.get<any>(`${messagesUrl}`, { headers });
    }

    private getExchanges(exchangesUrl: string, page: number, state: string,  is_offerer: boolean, is_requester: boolean): Observable< {exchange: Exchange[], pagination: Pagination}> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.exchanges.v1+json' });

        let params = new HttpParams()
            .set('state', state)
            .set('is_offerer', is_offerer ? 'true' : 'false')
            .set('is_requester', is_requester ? 'true' : 'false')
            .set('page', (page !== undefined && page !== null) ? page.toString() : '0');

        return this.http.get<any>(`${exchangesUrl}`, { headers, params, observe: 'response' }).pipe(
            map(response => {
                let totalRecords: Pagination = new Pagination(Number(response.headers.get('X-Total-Count')), Number(response.headers.get('X-Total-Pages')), Number(response.headers.get('X-Current-Page')));

                const exchanges: Exchange[] = response.body.map((exchange: any) => new Exchange(exchange));
                return {exchange: exchanges, pagination: totalRecords };
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
        return this.getExchanges(exchangesUrl, page, 'COMPLETED', true, true);
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

    rejectExchange(exchangeUrl: string, acceptCode: number, requester: boolean): Observable<any> {
        return this.updateExchange(exchangeUrl, acceptCode, requester, false);
    }


}