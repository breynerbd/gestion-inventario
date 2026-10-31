import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ProductResponse } from '../models/product-response';
import { PageResponse } from '../../../shared/models/page-response';
import { ProductCreate } from '../models/product-create';
import { ProductUpdate } from '../models/product-update';
import { ProductStatus } from '../models/product-status';
import { ProductFilters } from '../models/product-filters';

@Injectable({
    providedIn: 'root'
})
export class ProductService {

    private readonly apiUrl = '/api/products';

    constructor(private readonly http: HttpClient) { }

    getProducts(filters: ProductFilters = {}): Observable<PageResponse<ProductResponse>> {
        let params = new HttpParams().set("page", (filters.page ?? 0).toString()).set("size", (filters.size ?? 10).toString())
            .set("sort", filters.sort ?? "productCode,asc");

        if (filters.productCode?.trim()) { params = params.set("productCode", filters.productCode.trim()); }
        if (filters.productName?.trim()) { params = params.set("productName", filters.productName.trim()); }
        if (filters.categoryId) { params = params.set("categoryId", filters.categoryId); }
        if (filters.supplierId) { params = params.set("supplierId", filters.supplierId); }
        if (filters.status) { params = params.set("status", filters.status); }

        return this.http.get<PageResponse<ProductResponse>>(
            this.apiUrl,
            { params }
        );
    }

    getProductById(productId: number): Observable<ProductResponse> {
        return this.http.get<ProductResponse>(
            `${this.apiUrl}/${productId}`
        );
    }

    createProduct(product: ProductCreate): Observable<ProductResponse> {
        return this.http.post<ProductResponse>(
            this.apiUrl,
            product
        );
    }

    updateProduct(productId: number, product: ProductUpdate): Observable<ProductResponse> {
        return this.http.put<ProductResponse>(
            `${this.apiUrl}/${productId}`,
            product
        )
    }

    changeStatus(productId: number, status: ProductStatus): Observable<ProductResponse> {
        return this.http.patch<ProductResponse>(
            `${this.apiUrl}/${productId}/status`,
            status
        )
    }
}