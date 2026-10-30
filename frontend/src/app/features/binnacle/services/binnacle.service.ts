
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

import { BinnacleResponse } from "../models/binnacle-response";

@Injectable({
    providedIn: "root"
})
export class BinnacleService {

    private readonly apiUrl = "/api/binnacle";

    constructor(private readonly http: HttpClient) { }

    getLogs(): Observable<BinnacleResponse[]> {
        return this.http.get<BinnacleResponse[]>(this.apiUrl);
    }
}
