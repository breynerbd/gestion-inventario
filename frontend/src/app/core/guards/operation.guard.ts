import { inject } from '@angular/core';
import { CanActivateChildFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { AuthService } from '../../features/auth/services/auth.service';

export const operationGuard: CanActivateChildFn = route => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const requiredOperation = route.data['operation'] as string | undefined;

  return authService.loadOperations().pipe(
    map(operations => {
      if (!requiredOperation || operations.includes(requiredOperation)) {
        return true;
      }

      return router.createUrlTree(['/dashboard']);
    }),
    catchError(() => of(false))
  );
};
