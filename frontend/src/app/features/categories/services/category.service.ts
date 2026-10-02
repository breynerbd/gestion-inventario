import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { CategoryResponse } from '../models/category-response';
import { PageResponse } from '../../../shared/models/page-response';
import { CategoryCreate } from '../models/category-create';
import { CategoryUpdate } from '../models/category-update';
import { CategoryStatus } from '../models/category-status';

@Injectable({
    providedIn: 'root'
})
export class CategoryService {

    private readonly apiUrl = 'http://localhost:8080/api/categories';

    constructor(private readonly http: HttpClient) { }

    getCategories(): Observable<PageResponse<CategoryResponse>> {
        return this.http.get<PageResponse<CategoryResponse>>(this.apiUrl);
    }

    getCategoryId(categoryId: number): Observable<CategoryResponse> {
        return this.http.get<CategoryResponse>(
            `${this.apiUrl}/${categoryId}`
        );
    }

    createCategory(category: CategoryCreate): Observable<CategoryResponse>{
        return this.http.post<CategoryResponse>(
            this.apiUrl,
            category
        );
    }

    updateCategory(categoryId: number, category: CategoryUpdate): Observable<CategoryResponse> {
        return this.http.put<CategoryResponse>(
            `${this.apiUrl}/${categoryId}`,
            category
        );
    } 

    changeStatus(categoryId: number, status: CategoryStatus): Observable<CategoryResponse> {
        return this.http.patch<CategoryResponse>(
            `${this.apiUrl}/${categoryId}/status`,
            status
        )
    }
}