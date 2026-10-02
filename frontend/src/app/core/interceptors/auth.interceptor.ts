import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';

import { AuthService } from '../../features/auth/services/auth.service';

export const authInterceptor: HttpInterceptorFn = (request, next) => {
    const authService = inject(AuthService);
    const accessToken = authService.getAccessToken();

    if (accessToken) {
        const authenticatedRequest = request.clone({
            setHeaders: {
                Authorization: `Bearer ${accessToken}`
            }
        });

        return next(authenticatedRequest);
    }

    return next(request);
};