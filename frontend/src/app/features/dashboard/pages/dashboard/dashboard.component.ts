import {Component, ChangeDetectionStrategy, inject, DestroyRef} from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';
import { RoleAuthService } from '../../../../core/services/role-auth.service';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { catchError, EMPTY, exhaustMap, interval } from 'rxjs';
import { lucideMenu, lucideUser } from '@ng-icons/lucide';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, RouterOutlet, RouterLinkActive, NgIconComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css',
  changeDetection: ChangeDetectionStrategy.Eager,
  viewProviders: [provideIcons({ lucideMenu, lucideUser })]
})
export class DashboardComponent {
  username = "";
  roleName = "";
  isSidebarOpen = false;

  private readonly destroyRef = inject(DestroyRef);

  constructor(private readonly authService: AuthService, private readonly router: Router,
    private readonly roleService: RoleAuthService) {
    this.username = this.authService.getUsername() || "";
    this.roleName = this.authService.getRoleName() || "";

    interval(30_000).pipe(
      exhaustMap(() => this.authService.loadOperations().pipe(
        catchError(() => EMPTY)
      )),
      takeUntilDestroyed(this.destroyRef)
    ).subscribe();
  }

  toggleSidebar(): void {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  closeSidebar(): void {
    this.isSidebarOpen = false;
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

  canAccess(operation: string): boolean {
    return this.authService.hasOperation(operation);
  }

  logout(): void {
    this.authService.logout();
    void this.router.navigate(["/login"]);
  }

}
