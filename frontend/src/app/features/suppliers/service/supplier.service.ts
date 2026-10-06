import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { PageResponse } from '../../../shared/models/page-response';
import { SupplierResponse } from '../models/supplier-response';
import { SupplierCreate } from '../models/supplier-create';
import { SupplierUpdate } from '../models/supplier-update';
import { SupplierStatus } from '../models/supplier-status';

@Injectable({
    providedIn: 'root'
})
export class SupplierService {

    private readonly apiUrl = 'http://localhost:8080/api/suppliers';

    constructor(private readonly http: HttpClient) { }

    getSuppliers(page: number = 0, size: number = 10): Observable<PageResponse<SupplierResponse>> {
        const params = new HttpParams().set("page", page.toString()).set("size", size.toString());
        return this.http.get<PageResponse<SupplierResponse>>(this.apiUrl, { params });
    }

    getSupplierId(supplierId: number): Observable<SupplierResponse> {
        return this.http.get<SupplierResponse>(
            `${this.apiUrl}/${supplierId}`
        );
    }

    createSupplier(supplier: SupplierCreate): Observable<SupplierResponse>{
        return this.http.post<SupplierResponse>(
            this.apiUrl,
            supplier
        );
    }

    updateSupplier(supplierId: number, supplier: SupplierUpdate): Observable<SupplierResponse>{
        return this.http.put<SupplierResponse>(
            `${this.apiUrl}/${supplierId}`,
            supplier
        );
    }

    changeStatus(supplierId: number, status: SupplierStatus): Observable<SupplierResponse>{
        return this.http.patch<SupplierResponse>(
            `${this.apiUrl}/${supplierId}/status`,
            status
        )
    }
}