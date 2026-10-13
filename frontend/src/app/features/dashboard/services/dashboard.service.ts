
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { LowStockProduct } from "../models/low-stock-product";
import { MovementSummary } from "../models/movement-summary";
import { ProductsCount } from "../models/products-category";

@Injectable({
    providedIn: "root"
})
export class DashboardService {

    private apiProductsUrl = "http://localhost:8080/api/products";
    private apiMovementsUrl = "http://localhost:8080/api/movements"
    private apiSuppliersUrl = "http://localhost:8080/api/suppliers"

    constructor(private http: HttpClient){
    }

    getLowStockProducts(): Observable<LowStockProduct[]> {
        return this.http.get<LowStockProduct[]>(
            `${this.apiProductsUrl}/low-stock`
        );
    }

    getMovementsSummary(): Observable<MovementSummary[]> {
        return this.http.get<MovementSummary[]> (
            `${this.apiMovementsUrl}/summary`
        );
    }

    getProductsCount(): Observable<ProductsCount[]> {
        return this.http.get<ProductsCount[]> (
            `${this.apiProductsUrl}/products-category`
        );
    }

    getActiveProducts(): Observable<number> {
        return this.http.get<number> (
            `${this.apiProductsUrl}/active-products`
        );
    }

    getInventoryValue(): Observable<number> {
        return this.http.get<number> (
            `${this.apiProductsUrl}/inventory-value`
        )
    }

    getInventoryInvestment(): Observable<number> {
        return this.http.get<number> (
            `${this.apiProductsUrl}/inventory-investment`
        )
    }

    getActiveSuppliers(): Observable<number> {
        return this.http.get<number> (
            `${this.apiSuppliersUrl}/active-suppliers`
        );
    }
}