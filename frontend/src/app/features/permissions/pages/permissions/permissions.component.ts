import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";

import { PermissionResponse } from "../../models/permission-response";
import { PermissionCreate } from "../../models/permission-create";
import { PermissionUpdate } from "../../models/permission-update";
import { PermissionService } from "../../services/permission.service";
import { PaginationComponent } from "../../../../shared/components/pagination/pagination.component";
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { lucideBan, lucideCircleCheck, lucidePencilSparkles, lucidePlus } from '@ng-icons/lucide';

@Component({
  selector: "app-permissions",
  imports: [FormsModule, PaginationComponent, NgIconComponent],
  templateUrl: "./permissions.component.html",
  styleUrl: "./permissions.component.css",
  viewProviders: [provideIcons({ lucideBan, lucideCircleCheck, lucidePencilSparkles, lucidePlus })]
})
export class PermissionsComponent implements OnInit {

  permissions: PermissionResponse[] = [];

  showForm = false;
  editingPermissionId: number | null = null;
  errorMessage = "";
  errorMessageForm = "";
  searchTerm = "";

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

  get filteredPermissions(): PermissionResponse[] {
    const search = this.searchTerm.toLowerCase();

    return this.permissions.filter(permission => 
      permission.permissionId.toString().includes(search) ||
      permission.permissionCode.toLowerCase().includes(search) ||
      permission.module.toLowerCase().includes(search)
    )
  }

  get totalPages(): number {
    return Math.ceil(this.filteredPermissions.length / this.pageSize);
  }

  get paginatedPermissions(): PermissionResponse[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.filteredPermissions.slice(startIndex, startIndex + this.pageSize);
  }

  onSearchChange(value: string) {
    this.searchTerm = value;
    this.currentPage = 1;
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
    this.errorMessageForm = "";

    if (!this.newPermission.permissionName.trim()) {
      this.errorMessageForm = "El nombre del permiso es obligatorio.";
      return;
    }

    if (this.editingPermissionId === null) {
      if (!this.newPermission.permissionCode?.trim()) {
        this.errorMessageForm = "El código del permiso es obligatorio.";
        return;
      }

      if (!this.newPermission.module) {
        this.errorMessageForm = "Debes seleccionar un módulo.";
        return;
      }

      this.createPermission();
    } else {
      if (!this.newPermission.module) {
        this.errorMessageForm = "Debes seleccionar un módulo.";
        return;
      }

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
    this.errorMessageForm = "";
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