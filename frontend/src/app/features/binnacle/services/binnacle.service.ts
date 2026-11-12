
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { AuditLogResponse } from "../models/audit-log-response";
import { PageResponse } from "../../../shared/models/page-response";

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

    getChanges (page: number = 0, size: number = 12) : Observable<PageResponse<AuditLogResponse>> {
        return this.http.get<PageResponse<AuditLogResponse>>(
            `${this.apiUrl}/changes`,
            {
              params: {
                page, size, sort: "auditId,desc"
              }

            }
        )
    }
}
