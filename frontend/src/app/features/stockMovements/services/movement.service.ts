import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { PageResponse } from "../../../shared/models/page-response";
import { MovementResponse } from "../models/movement-response";
import { MovementCreate } from "../models/movement-create";
import { MovementUpdate } from "../models/movement-update";
import { MovementStatus } from "../models/movement-status";

@Injectable({
    providedIn: 'root'
})
export class MovementService {

    private readonly apiUrl = 'http://localhost:8080/api/movements'

    constructor(private readonly http: HttpClient){
    }

    getMovements(page: number = 0, size: number = 10): Observable<PageResponse<MovementResponse>> {
        const params = new HttpParams().set("page", page.toString()).set("size", size.toString());
        return this.http.get<PageResponse<MovementResponse>>(this.apiUrl, { params });
    }

    getMovementId(movementId: number): Observable<MovementResponse> {
        return this.http.get<MovementResponse>(
            `${this.apiUrl}/${movementId}`
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