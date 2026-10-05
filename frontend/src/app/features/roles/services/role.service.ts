import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { PageResponse } from "../../../shared/models/page-response";
import { RoleResponse } from "../models/role-response";
import { RoleCreate } from "../models/role-create";
import { RoleUpdate } from "../models/role-update";
import { RoleStatus } from "../models/role-status";

@Injectable({
    providedIn: 'root'
})
export class RoleService {
    private readonly apiUrl = 'http://localhost:8080/api/roles';

    constructor(private readonly http: HttpClient){
    }

    getRoles(): Observable<RoleResponse[]> {
        return this.http.get<RoleResponse[]>(this.apiUrl);
    }

    getRoleId(roleId: number): Observable<RoleResponse> {
        return this.http.get<RoleResponse>(
            `${this.apiUrl}/${roleId}`
        );
    }

    createRole(role: RoleCreate): Observable<RoleResponse> {
        return this.http.post<RoleResponse>(
            this.apiUrl,
            role
        );
    }

    updateRole(roleId: number, role: RoleUpdate): Observable<RoleResponse> {
        return this.http.put<RoleResponse>(
            `${this.apiUrl}/${roleId}`,
            role
        );
    }

    changeStatus(roleId: number, status: RoleStatus): Observable<RoleResponse> {
        return this.http.patch<RoleResponse>(
            `${this.apiUrl}/${roleId}/status`,
            status
        );
    }
}