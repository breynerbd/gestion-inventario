import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn, HttpResponse } from '@angular/common/http';

import { AuthService } from '../../features/auth/services/auth.service';
import { catchError, switchMap, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
    const authService = inject(AuthService);
    const accessToken = authService.getAccessToken();

    if(!accessToken) {
        return next(request);
    }

    const authenticatedRequest = request.clone({
        setHeaders: {
            Authorization: `Bearer ${accessToken}`
        }
    });

    return next(authenticatedRequest).pipe(
        catchError((error: HttpErrorResponse) => {
            if((error.status !== 401 && error.status !== 403) || request.url.includes("/refresh")) {
                return throwError(() => error);
            }

            const refreshToken = localStorage.getItem("refreshToken");

            if(!refreshToken) {
                authService.logout();
                return throwError(() => error);
            }

            return authService.refreshToken(refreshToken).pipe(
                switchMap((response) => {
                    authService.saveSession(response);

                    const newRequest = request.clone({
                        setHeaders: {
                            Authorization: `Bearer ${response.accessToken}`
                        }
                    });

                    return next(newRequest);
                }),
                catchError((refreshError) => {
                    authService.logout();
                    return throwError(() => refreshError);
                })
            );
        })
    );
};