
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { LowStockProduct } from "../models/low-stock-product";

@Injectable({
    providedIn: "root"
})
export class DashboardService {

    private apiUrl = "http://localhost:8080/api/products";

    constructor(private http: HttpClient){
    }

    getLowStockProducts(): Observable<LowStockProduct[]> {
        return this.http.get<LowStockProduct[]>(
            `${this.apiUrl}/low-stock`
        );
    }
}