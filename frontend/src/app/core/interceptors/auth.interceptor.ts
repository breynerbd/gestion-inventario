import { inject } from '@angular/core';
import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';

import { AuthService } from '../../features/auth/services/auth.service';
import { catchError, switchMap, throwError } from 'rxjs';
import {Router} from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
    const authService = inject(AuthService);
    const accessToken = authService.getAccessToken();

    const router = inject(Router);

    const endSession = () => {
      authService.logout();
      void router.navigate(['/login'], { replaceUrl: true });
    };

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
                endSession()
                return throwError(() => error);
            }

            return authService.refreshToken(refreshToken).pipe(

                catchError((refreshError) => {
                  endSession()
                  return throwError(() => refreshError);
                }),
                switchMap((response) => {
                    authService.saveSession(response);

                    const newRequest = request.clone({
                        setHeaders: {
                            Authorization: `Bearer ${response.accessToken}`
                        }
                    });

                    return next(newRequest);
                })
            );
        })
    );
};
