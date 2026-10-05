import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { UserResponse } from "../models/user-response";
import { UserCreate } from "../models/user-create";
import { UserUpdate } from "../models/user-update";
import { UserStatus } from "../models/user-status";

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private readonly apiUrl = 'http://localhost:8080/api/users';

    constructor(private readonly http: HttpClient){
    }

    getUsers(): Observable<UserResponse[]>{
        return this.http.get<UserResponse[]>(this.apiUrl);
    }

    getUserId(userId: number): Observable<UserResponse> {
        return this.http.get<UserResponse>(
            `${this.apiUrl}/${userId}`
        );
    }

    createUser(user: UserCreate): Observable<UserResponse> {
        return this.http.post<UserResponse>(
            this.apiUrl,
            user
        );
    }

    updateUser(userId: number, user: UserUpdate): Observable<UserResponse> {
        return this.http.put<UserResponse>(
            `${this.apiUrl}/${userId}`,
            user
        );
    }

    changeStatus(userId: number, status: UserStatus): Observable<UserResponse> {
        return this.http.patch<UserResponse>(
            `${this.apiUrl}/${userId}/status`,
            status
        );
    }
}