import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";

import { PermissionResponse } from "../../models/permission-response";
import { PermissionCreate } from "../../models/permission-create";
import { PermissionUpdate } from "../../models/permission-update";
import { PermissionService } from "../../services/permission.service";

@Component({
  selector: "app-permissions",
  imports: [FormsModule],
  templateUrl: "./permissions.component.html",
  styleUrl: "./permissions.component.css"
})
export class PermissionsComponent implements OnInit {

  permissions: PermissionResponse[] = [];

  showForm = false;
  editingPermissionId: number | null = null;
  errorMessage = "";

  currentPage = 1;
  pageSize = 10;

  newPermission: PermissionCreate = {
    permissionCode: "",
    permissionName: "",
    module: "",
    description: ""
  };

  constructor(private readonly permissionService: PermissionService) { }

  ngOnInit(): void {
    this.loadPermissions();
  }

  loadPermissions(): void {
    this.permissionService.getPermissions().subscribe({
      next: (response) => {
        this.permissions = response;
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los permisos";
      }
    });
  }

  get totalPages(): number {
    return Math.ceil(this.permissions.length / this.pageSize);
  }

  get paginatedPermissions(): PermissionResponse[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.permissions.slice(startIndex, startIndex + this.pageSize);
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

  createPermission(): void {
    this.errorMessage = "";

    this.permissionService.createPermission(this.newPermission).subscribe({
      next: () => {
        this.showForm = false;
        this.resetForm();
        this.loadPermissions();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo crear el permiso";
      }
    });
  }

  editPermission(permission: PermissionResponse): void {
    this.editingPermissionId = permission.permissionId;
    this.showForm = true;
    this.errorMessage = "";

    this.newPermission = {
      permissionCode: permission.permissionCode,
      permissionName: permission.permissionName,
      module: permission.module,
      description: permission.description
    };
  }

  updatePermission(): void {
    if (this.editingPermissionId === null) {
      return;
    }

    const permission: PermissionUpdate = {
      permissionName: this.newPermission.permissionName,
      module: this.newPermission.module,
      description: this.newPermission.description
    };

    this.permissionService.updatePermission(
      this.editingPermissionId,
      permission
    ).subscribe({
      next: () => {
        this.showForm = false;
        this.editingPermissionId = null;
        this.resetForm();
        this.loadPermissions();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo actualizar el permiso";
      }
    });
  }

  savePermission(): void {
    this.errorMessage = "";

    if (this.editingPermissionId === null) {
      this.createPermission();
    } else {
      this.updatePermission();
    }
  }

  changeStatus(permission: PermissionResponse): void {
    const newStatus = permission.status === "ACTIVO"
      ? "INACTIVO"
      : "ACTIVO";

    this.permissionService.changeStatus(
      permission.permissionId,
      { status: newStatus }
    ).subscribe({
      next: () => {
        this.loadPermissions();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo cambiar el estado del permiso";
      }
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editingPermissionId = null;
    this.errorMessage = "";
    this.resetForm();
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingPermissionId = null;
    this.resetForm();
  }

  resetForm(): void {
    this.newPermission = {
      permissionCode: "",
      permissionName: "",
      module: "",
      description: ""
    };
  }
}