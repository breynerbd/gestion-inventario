import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { LoginRequest } from '../models/login-request';
import { LoginResponse } from '../models/login-response';
import { RegisterRequest } from '../models/register-request';
import { RefreshTokenRequest } from '../models/refresh-token-request';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = "/api/auth"
  private readonly operations = signal<string[]>([]);

  constructor(private readonly http: HttpClient) {

  }

  login(request: LoginRequest): Observable<LoginResponse>{
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      request
    );
  }

  register(request: RegisterRequest): Observable<LoginResponse>{
    return this.http.post<LoginResponse>(
      `${this.apiUrl}/register`,
      request
    );
  }

  refreshToken(refreshToken: string): Observable<LoginResponse>{
    const request: RefreshTokenRequest = {
      refreshToken: refreshToken
    };

    return this.http.post<LoginResponse>(
      `${this.apiUrl}/refresh`,
      request
    );
  }

  saveSession(response: LoginResponse): void{
    localStorage.setItem("accessToken", response.accessToken)
    localStorage.setItem("refreshToken", response.refreshToken)
    localStorage.setItem("username", response.username)
    localStorage.setItem("roleName", response.roleName)
    localStorage.setItem("mustChangePassword", String(response.mustChangePassword));
  }

  getAccessToken = (): string | null => localStorage.getItem('accessToken');
  getUsername = (): string | null => localStorage.getItem('username');
  getRoleName = (): string | null => localStorage.getItem('roleName');

  isAuthenticated(): boolean {
    return !!this.getAccessToken();
  }

  loadOperations() : Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/operations`).pipe(
      tap({
        next: operations => this.operations.set(operations),
        error: () => this.operations.set([])
      })
    );
  }

  hasOperation(operation: string): boolean {
    return this.operations().includes(operation);
  }

  logout(): void {
    this.operations.set([]);
    ['accessToken', 'refreshToken', 'username', 'roleName', 'mustChangePassword'].forEach(key => {
      localStorage.removeItem(key);
    });
  }

  mustChangePassword(): boolean {
    return localStorage.getItem("mustChangePassword") === "true";
  }
}
