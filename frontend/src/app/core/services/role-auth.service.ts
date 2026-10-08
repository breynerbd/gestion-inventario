import { Injectable } from "@angular/core";
import { AuthService } from "../../features/auth/services/auth.service";

@Injectable({
    providedIn: 'root'
})
export class RoleAuthService {
    constructor(private readonly authService: AuthService) {
    }

    isAdmin(): boolean {
        return this.authService.getRoleName() === "ADMINISTRADOR";
    }

    isSupervisor(): boolean {
        return this.authService.getRoleName() === "SUPERVISOR";
    }

    isOperator(): boolean {
        return this.authService.getRoleName() === "OPERADOR";
    }

    manageUsers(): boolean {
        return this.isAdmin();
    }

    manageRoles(): boolean {
        return this.isAdmin();
    }

    managePermissions(): boolean {
        return this.isAdmin();
    }

    createProduct(): boolean {
        return this.isAdmin() || this.isSupervisor();
    }

    editProduct(): boolean {
        return this.isAdmin() || this.isSupervisor();
    }

    createCategory(): boolean {
        return this.isAdmin() || this.isSupervisor();
    }

    editCategory(): boolean {
        return this.isAdmin() || this.isSupervisor();
    }

    createSupplier(): boolean {
        return this.isAdmin() || this.isSupervisor();
    }

    editSupplier(): boolean {
        return this.isAdmin() || this.isSupervisor();
    }

    createMovement(): boolean {
        return this.isAdmin() || this.isOperator();
    }

    editMovement(): boolean {
        return this.isAdmin();
    }

    getRoleName(): string | null {
        return this.authService.getRoleName();
    }
}