import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ProductResponse } from '../models/product-response';
import { PageResponse } from '../../../shared/models/page-response';
import { ProductCreate } from '../models/product-create';
import { ProductUpdate } from '../models/product-update';
import { ProductStatus } from '../models/product-status';

@Injectable({
    providedIn: 'root'
})
export class ProductService {

    private readonly apiUrl = 'http://localhost:8080/api/products';

    constructor(private readonly http: HttpClient) { }

    getProducts(page: number = 0, size: number = 10, 
        productCode: string = "", productName: string = "", categoryId?: number, 
        supplierId?: number, status?: string
    ): Observable<PageResponse<ProductResponse>> {
        let params = new HttpParams().set("page", page.toString()).set("size", size.toString());

        if(productCode?.trim()) params = params.set("productCode", productCode.trim());
        if(productName?.trim()) params = params.set("productName", productName.trim());
        if(categoryId) params = params.set("categoryId", categoryId);
        if(supplierId) params = params.set("supplierId", supplierId);
        if(status) params = params.set("status", status);

        return this.http.get<PageResponse<ProductResponse>>(this.apiUrl, { params });
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