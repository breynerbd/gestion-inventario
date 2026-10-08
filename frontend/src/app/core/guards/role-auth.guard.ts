import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { RoleAuthService } from "../services/role-auth.service";

export const roleAuthGuard = (allowedRoles: string[]): CanActivateFn => {
    return () => {
        const roleService = inject(RoleAuthService);
        const router = inject(Router);
        const role = roleService["authService"].getRoleName();

        if(role && allowedRoles.includes(role)) {
            return true;
        }

        return router.createUrlTree(["/dashboard"])
    }
}