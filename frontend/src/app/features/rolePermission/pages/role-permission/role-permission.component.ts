
import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { DatePipe } from "@angular/common";
import { NgIconComponent, provideIcons } from "@ng-icons/core";
import { lucidePlus } from "@ng-icons/lucide";

import { PaginationComponent } from "../../../../shared/components/pagination/pagination.component";

import { RoleService } from "../../../roles/services/role.service";
import { PermissionService } from "../../../permissions/services/permission.service";
import { RolePermissionService } from "../../services/role-permission.service";

import { RoleResponse } from "../../../roles/models/role-response";
import { PermissionResponse } from "../../../permissions/models/permission-response";
import { RolePermissionResponse } from "../../models/role-permission-response";

@Component({
  selector: "app-role-permissions",
  standalone: true,
  imports: [FormsModule, DatePipe, NgIconComponent, PaginationComponent],
  providers: [provideIcons({ lucidePlus })],
  templateUrl: "./role-permission.component.html",
  styleUrl: "./role-permission.component.css"
})
export class RolePermissionsComponent implements OnInit {

  roles: RoleResponse[] = [];
  permissions: PermissionResponse[] = [];
  rolePermissions: RolePermissionResponse[] = [];

  selectedRoleId: number | null = null;
  selectedPermissionIds: number[] = [];

  searchTerm = "";

  currentPage = 1;
  pageSize = 12;

  showForm = false;
  loading = false;

  errorMessage = "";

  constructor(private readonly roleService: RoleService, private readonly permissionService: PermissionService,
    private readonly rolePermissionService: RolePermissionService, private readonly changeDetectorRef: ChangeDetectorRef
  ) {

  }

  ngOnInit(): void {
    this.loadRoles();
    this.loadPermissions();
  }

  loadRoles(): void {
    this.roleService.getRoles().subscribe({
      next: (response) => {
        this.roles = response;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.errorMessage = "Error al cargar los roles.";
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  loadPermissions(): void {
    this.permissionService.getPermissions().subscribe({
      next: (response) => {
        this.permissions = response;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.errorMessage = "Error al cargar los permisos.";
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  loadRolePermissions(): void {
    if (this.selectedRoleId === null) {
      this.rolePermissions = [];
      this.changeDetectorRef.detectChanges();
      return;
    }

    this.loading = true;
    this.errorMessage = "";

    this.rolePermissionService.getRolePermissions(this.selectedRoleId).subscribe({
      next: (response) => {
        this.rolePermissions = response;
        this.loading = false;
        this.currentPage = 1;
        this.changeDetectorRef.detectChanges();
      },
      error: () => {
        this.errorMessage = "Error al cargar los permisos del rol.";
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  onRoleChange(): void {
    this.loadRolePermissions();
  }

  openForm(): void {
    if (this.selectedRoleId === null) {
      this.errorMessage = "Selecciona un rol antes de asignar permisos.";
      return;
    }

    this.selectedPermissionIds = this.rolePermissions.map(
      permission => permission.permissionId
    );

    this.errorMessage = "";
    this.showForm = true;
  }

  closeForm(): void {
    this.showForm = false;
    this.selectedPermissionIds = [];
  }

  togglePermission(permissionId: number): void {
    if (this.selectedPermissionIds.includes(permissionId)) {
      this.selectedPermissionIds = this.selectedPermissionIds.filter(
        id => id !== permissionId
      );
    } else {
      this.selectedPermissionIds.push(permissionId);
    }
  }

  isPermissionSelected(permissionId: number): boolean {
    return this.selectedPermissionIds.includes(permissionId);
  }

  savePermissions(): void {
    if (this.selectedRoleId === null) {
      return;
    }

    this.loading = true;
    this.errorMessage = "";

    this.rolePermissionService.assignPermissions(
      this.selectedRoleId,
      {
        permissionIds: this.selectedPermissionIds
      }
    ).subscribe({
      next: (response) => {
        this.rolePermissions = response;
        this.loading = false;
        this.showForm = false;
        this.currentPage = 1;
        this.changeDetectorRef.detectChanges();
      },
      error: (error) => {
        this.errorMessage = error.error?.message || "Error al asignar los permisos al rol.";
        this.loading = false;
        this.changeDetectorRef.detectChanges();
      }
    });
  }

  get filteredPermissions(): RolePermissionResponse[] {
    const search = this.searchTerm.trim().toLowerCase();

    return this.rolePermissions.filter(permission =>
      permission.permissionId.toString().includes(search) ||
      permission.permissionCode.toLowerCase().includes(search) ||
      permission.permissionName.toLowerCase().includes(search) ||
      permission.module.toLowerCase().includes(search)
    );
  }

  get paginatedPermissions(): RolePermissionResponse[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;

    return this.filteredPermissions.slice(
      startIndex,
      startIndex + this.pageSize
    );
  }

  get totalPages(): number {
    return Math.ceil(this.filteredPermissions.length / this.pageSize);
  }

  onSearchChange(): void {
    this.currentPage = 1;
  }

  onPageChange(page: number): void {
    this.currentPage = page;
  }

  get selectedRole(): RoleResponse | undefined {
    return this.roles.find(role => role.roleId === this.selectedRoleId);
  }

  get canAssignPermissions(): boolean {
    return this.selectedRole?.status === "ACTIVO";
  }
}
