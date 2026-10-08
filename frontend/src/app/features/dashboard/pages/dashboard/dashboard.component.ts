import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';
import { RoleAuthService } from '../../../../core/services/role-auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, RouterOutlet, RouterLinkActive],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  username = "";
  roleName = "";

  constructor(private readonly authService: AuthService, private readonly router: Router,
    private readonly roleService: RoleAuthService) {
    this.username = this.authService.getUsername() || "";
    this.roleName = this.authService.getRoleName() || "";
  }

  canManageUsers(): boolean {
    return this.roleService.manageUsers();
  }

  canManageRoles(): boolean {
    return this.roleService.manageRoles();
  }

  canManagePermissions(): boolean {
    return this.roleService.managePermissions();
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(["/login"]);
  }

}
