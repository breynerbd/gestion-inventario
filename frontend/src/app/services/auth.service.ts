import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../models/auth/login-request';
import { Observable } from 'rxjs';
import { LoginResponse } from '../models/auth/login-response';
import { RegisterRequest } from '../models/auth/register-request';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = "http://localhost:8080/api/auth"

  constructor(private http: HttpClient) {

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
