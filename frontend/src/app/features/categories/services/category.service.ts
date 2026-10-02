import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { CategoryResponse } from '../models/category-response';
import { PageResponse } from '../../../shared/models/page-response';

@Injectable({
    providedIn: 'root'
})
export class CategoryService {

    private readonly apiUrl = 'http://localhost:8080/api/categories';

    constructor(private readonly http: HttpClient) { }

    getCategories(): Observable<PageResponse<CategoryResponse>> {
        return this.http.get<PageResponse<CategoryResponse>>(this.apiUrl);
    }
}