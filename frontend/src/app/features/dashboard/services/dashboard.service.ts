
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { LowStockProduct } from "../models/low-stock-product";
import { MovementSummary } from "../models/movement-summary";

@Injectable({
    providedIn: "root"
})
export class DashboardService {

    private apiUrl = "http://localhost:8080/api/products";
    private apiMovementsUrl = "http://localhost:8080/api/movements"

    constructor(private http: HttpClient){
    }

    getLowStockProducts(): Observable<LowStockProduct[]> {
        return this.http.get<LowStockProduct[]>(
            `${this.apiUrl}/low-stock`
        );
    }

    getMovementsSummary(): Observable<MovementSummary[]> {
        return this.http.get<MovementSummary[]> (
            `${this.apiMovementsUrl}/summary`
        )
    }
}