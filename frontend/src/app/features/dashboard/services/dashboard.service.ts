import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { LowStockProduct } from "../models/low-stock-product";
import { MovementSummary } from "../models/movement-summary";
import { ProductsCount } from "../models/products-category";
import { PageResponse } from "../../../shared/models/page-response";
import { ProductResponse } from "../../products/models/product-response";

@Injectable({
    providedIn: "root"
})
export class DashboardService {

    private readonly apiProductsUrl = "/api/products";
    private readonly apiMovementsUrl = "/api/movements";
    private readonly apiSuppliersUrl = "/api/suppliers";

    constructor(private readonly http: HttpClient){
    }

    getLowStockProducts(): Observable<LowStockProduct[]> {
        return this.http.get<LowStockProduct[]>(
            `${this.apiProductsUrl}/low-stock`
        );
    }

    getMovementsSummary(startDate?: string, endDate?: string, groupBy: string = "MONTH"): Observable<MovementSummary[]> {
        let params = new HttpParams().set('groupBy', groupBy);

        if (startDate) {
            params = params.set('startDate', startDate);
        }

        if (endDate) {
            params = params.set('endDate', endDate);
        }

        return this.http.get<MovementSummary[]> (
            `${this.apiMovementsUrl}/summary`,
            { params }
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

    getMovementsValue(): Observable<number> {
        return this.http.get<number> (
            `${this.apiMovementsUrl}/movements-value`
        );
    }

    getProductsByMovements(): Observable<number> {
        return this.http.get<number> (
            `${this.apiMovementsUrl}/products-movement`
        );
    }

    getActiveSuppliers(): Observable<number> {
        return this.http.get<number> (
            `${this.apiSuppliersUrl}/active-suppliers`
        );
    }

    getProductsByCategory(categoryId: number): Observable<PageResponse<ProductResponse>> {
        const params = new HttpParams()
            .set("categoryId", categoryId.toString())
            .set("status", "ACTIVO")
            .set("page", "0")
            .set("size", "100");

        return this.http.get<PageResponse<ProductResponse>>(
            this.apiProductsUrl,
            { params }
        );
    }
}
