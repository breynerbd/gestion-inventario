import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { PageResponse } from '../../../shared/models/page-response';
import { SupplierResponse } from '../models/supplier-response';

@Injectable({
    providedIn: 'root'
})
export class SupplierService {

    private readonly apiUrl = 'http://localhost:8080/api/suppliers';

    constructor(private readonly http: HttpClient) { }

    getSuppliers(): Observable<PageResponse<SupplierResponse>> {
        return this.http.get<PageResponse<SupplierResponse>>(this.apiUrl);
    }
}