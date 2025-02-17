import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import {environment} from "../../../environments/environment";
import {map} from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class ImageService {
    baseUrl = (environment.production ? environment.productionUrl : environment.developmentUrl) + '/images';


    constructor(private http: HttpClient) {}

    uploadImage(imageFile: File): Observable<string> {
        const formData = new FormData();
        formData.append('image', imageFile);

        return this.http.post(this.baseUrl, formData, {
            observe: 'response'
        }).pipe(
            map(response => response.headers.get('Location') || '')
        );
    }


}
