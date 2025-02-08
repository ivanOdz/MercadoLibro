import {Exchange} from "../models/exchange.model";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {map} from "rxjs/operators";
import {Observable, tap} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({ providedIn: 'root' })
export class ExchangeService {
    baseUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient) { }


    getMessages(messagesUrl: string): Observable<any> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.message.v1+json' });
        return this.http.get<any>(`${this.baseUrl}${messagesUrl}`, { headers });
    }

    //  /exchanges?user_id=123546789&state=accepted&isRequester=true&isOfferer=true&page=1
    //  exchangesUrl: /exchanges?user_id=123546789
    getActiveExchanges(exchangesUrl: string, page: number): Observable<Exchange[]> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.exchanges.v1+json' });

        let params = new HttpParams()
            .set('state', 'ACCEPTED')
            .set('is_offerer', 'true')
            .set('is_requester', 'true')
            .set('page', page.toString());

        return this.http.get<any>(`${this.baseUrl}${exchangesUrl}`, { headers, params }).pipe(
            tap((e) => console.log("Respuesta de la API de exchanges:", e)),
            map((e) => e.map((exchange: any) => new Exchange(exchange)))
        );
    }

    // /exchanges?user_id=12345678state=pending&isOfferer=false&isRequester=true
    getSolicitedExchanges(exchangesUrl: string, page:number): Observable<Exchange[]> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.exchanges.v1+json'});

        console.log(headers);

        return this.http.get<any>( this.baseUrl + exchangesUrl
            + '&' + 'state=pending'
            + '&' + 'isOfferer=false'
            + '&' + 'isRequester=true'
            + '&' + 'page=' + page
            , { headers }).pipe(
            map((e) => {
                return e.map((exchange: any) => new Exchange(exchange));
            })
        );
    }

    // /exchanges?user_id=12345678state=pending&isOfferer=true&isRequester=false
    getExchangesOffers(exchangesUrl: string, page:number): Observable<Exchange[]> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.exchanges.v1+json'});

        console.log(headers);

        return this.http.get<any>( this.baseUrl + exchangesUrl
            + '&' + 'state=pending'
            + '&' + 'isOfferer=true'
            + '&' + 'isRequester=false'
            + '&' + 'page=' + page
            , { headers }).pipe(
            map((e) => {
                return e.map((exchange: any) => new Exchange(exchange));
            })
        );
    }

    // /exchanges?user_id=12345678&state=completed&isOfferer=true&isRequester=true&page=1
    getCompletedExchanges(exchangesUrl: string, page:number):Observable<Exchange[]>{
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.exchanges.v1+json'});

        console.log(headers);

        return this.http.get<any>( this.baseUrl + exchangesUrl
            + '&' + 'state=completed'
            + '&' + 'isOfferer=true'
            + '&' + 'isRequester=true'
            + '&' + 'page=' + page
            , { headers }).pipe(
            map((e) => {
                return e.map((exchange: any) => new Exchange(exchange));
            })
        );
    }
    // /exchanges?user_id=12345678&state=rejected&isOfferer=true&isRequester=true&page=1
    getRejectedExchanges(exchangesUrl: string, page:number):Observable<Exchange[]>{
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.exchanges.v1+json'});

        console.log(headers);

        return this.http.get<any>( this.baseUrl + exchangesUrl
            + '&' + 'state=rejected'
            + '&' + 'isOfferer=true'
            + '&' + 'isRequester=true'
            + '&' + 'page=' + page
            , { headers }).pipe(
            map((e) => {
                return e.map((exchange: any) => new Exchange(exchange));
            })
        );
    }

}