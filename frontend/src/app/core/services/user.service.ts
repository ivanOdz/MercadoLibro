import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { User } from "../models/user.model";
import { map } from "rxjs/operators";
import { Observable } from "rxjs";

@Injectable({ providedIn: 'root' })



export class UserService {


    constructor(private http: HttpClient) { }

    baseUrl = 'http://localhost:8080/api/';

    getUser(userUrl: string): Observable<User> {
        const headers = new HttpHeaders({ 'Accept': 'application/vnd.users.v1+json'});

        console.log(headers);

        return this.http.get<any>( this.baseUrl + userUrl, { headers }).pipe(
            map((userData) => {
                return new User(userData);
            })
        );
    }
}
