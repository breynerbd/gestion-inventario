
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { RolePermissionResponse } from "../models/role-permission-response";
import { RolePermissionRequest } from "../models/role-permission-request";

@Injectable({
    providedIn: 'root'
})
export class RolePermissionService {

    private readonly apiUrl = '/api/roles';

    constructor(private readonly http: HttpClient) { }

    getRolePermissions(roleId: number): Observable<RolePermissionResponse[]> {
        return this.http.get<RolePermissionResponse[]>(
            `${this.apiUrl}/${roleId}/permissions`
        );
    }

    assignPermissions(roleId: number, permissionIds: RolePermissionRequest): Observable<RolePermissionResponse[]> {
        return this.http.put<RolePermissionResponse[]>(
            `${this.apiUrl}/${roleId}/permissions`,
            permissionIds
        );
    }
}
