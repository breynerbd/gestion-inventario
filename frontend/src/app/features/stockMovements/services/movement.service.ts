import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { PageResponse } from "../../../shared/models/page-response";
import { MovementResponse } from "../models/movement-response";
import { MovementCreate } from "../models/movement-create";
import { MovementUpdate } from "../models/movement-update";
import { MovementStatus } from "../models/movement-status";
import { MovementFilters } from '../models/movement-filters';

@Injectable({
    providedIn: 'root'
})
export class MovementService {

    private readonly apiUrl = '/api/movements'

    constructor(private readonly http: HttpClient) {
    }

    getMovements(filters: MovementFilters = {}): Observable<PageResponse<MovementResponse>> {
        let params = new HttpParams().set("page", (filters.page ?? 0).toString()).set("size", (filters.size ?? 10).toString())
            .set("sort", filters.sort ?? "movementId,asc");

        if (filters.productId) { params = params.set("productId", filters.productId); }
        if (filters.movementType?.trim()) { params = params.set("movementType", filters.movementType.trim()); }
        if (filters.startDate?.trim()) { params = params.set("startDate", filters.startDate.trim()); }
        if (filters.endDate?.trim()) { params = params.set("endDate", filters.endDate.trim()); }
        if (filters.userId) { params = params.set("userId", filters.userId); }

        return this.http.get<PageResponse<MovementResponse>>(
            this.apiUrl,
            { params }
        );
    }

    getMovementId(movementId: number): Observable<MovementResponse> {
        return this.http.get<MovementResponse>(
            `${this.apiUrl}/${movementId}`
        );
    }

    getLatestMovements(): Observable<PageResponse<MovementResponse>> {
        const params = new HttpParams().set("page", "0").set("size", "5")
            .set("sort", "movementDate,desc");

        return this.http.get<PageResponse<MovementResponse>>(
            this.apiUrl, { params }
        );
    }

    createMovement(movement: MovementCreate): Observable<MovementResponse> {
        return this.http.post<MovementResponse>(
            this.apiUrl,
            movement
        );
    }

    updateMovement(movementId: number, movement: MovementUpdate): Observable<MovementResponse> {
        return this.http.put<MovementResponse>(
            `${this.apiUrl}/${movementId}`,
            movement
        );
    }

    changeStatus(movementId: number, status: MovementStatus): Observable<MovementResponse> {
        return this.http.patch<MovementResponse>(
            `${this.apiUrl}/${movementId}/status`,
            status
        );
    }
}
