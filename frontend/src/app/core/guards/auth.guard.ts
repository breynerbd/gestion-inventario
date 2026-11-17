import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../features/auth/services/auth.service';

export const authGuard: CanActivateFn = route => {

  const authService = inject(AuthService);
  const router = inject(Router);

  if(!authService.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }

  if (authService.mustChangePassword()
    && route.routeConfig?.path !== 'change-password') {
    return router.createUrlTree(['/change-password']);
  }

  return true;
};
