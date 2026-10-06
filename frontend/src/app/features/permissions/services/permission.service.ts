import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { PermissionResponse } from "../models/permission-response";
import { PermissionCreate } from "../models/permission-create";
import { PermissionStatus } from "../models/permission-status";

@Injectable({
    providedIn: 'root'
})
export class PermissionService {
    private readonly apiUrl = 'http://localhost:8080/api/permissions';

    constructor(private readonly http: HttpClient){
    }

    getPermissions(): Observable<PermissionResponse[]> {
        return this.http.get<PermissionResponse[]>(this.apiUrl);
    }

    getPermissionId(permissionId: number): Observable<PermissionResponse> {
        return this.http.get<PermissionResponse>(
            `${this.apiUrl}/${permissionId}`
        );
    }

    createPermission(permission: PermissionCreate): Observable<PermissionResponse> {
        return this.http.post<PermissionResponse>(
            this.apiUrl,
            permission
        );
    }

    updatePermission(permissionId: number, permission: PermissionCreate): Observable<PermissionResponse> {
        return this.http.put<PermissionResponse>(
            `${this.apiUrl}/${permissionId}`,
            permission
        );
    }

    changeStatus(permissionId: number, status: PermissionStatus): Observable<PermissionResponse> {
        return this.http.patch<PermissionResponse>(
            `${this.apiUrl}/${permissionId}/status`,
            status
        );
    }
}