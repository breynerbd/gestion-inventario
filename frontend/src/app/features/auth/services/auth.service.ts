import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../models/login-request';
import { LoginResponse } from '../models/login-response';
import { RegisterRequest } from '../models/register-request';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = "http://localhost:8080/api/auth"

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

  saveSession(response: LoginResponse): void{
    localStorage.setItem("accessToken", response.accessToken)
    localStorage.setItem("refreshToken", response.refreshToken)
    localStorage.setItem("username", response.username)
    localStorage.setItem("roleName", response.roleName)
  }

  getAccessToken = (): string | null => localStorage.getItem('accessToken');
  getUsername = (): string | null => localStorage.getItem('username');
  getRoleName = (): string | null => localStorage.getItem('roleName');

  isAuthenticated(): boolean {
    return !!this.getAccessToken();
  }

  logout(): void {
    ['accessToken', 'refreshToken', 'username', 'roleName'].forEach(key => {
      localStorage.removeItem(key);
    });
  }
}
