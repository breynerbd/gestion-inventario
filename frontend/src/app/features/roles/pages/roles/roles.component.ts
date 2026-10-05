import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RoleResponse } from '../../models/role-response';
import { RoleCreate } from '../../models/role-create';
import { RoleService } from '../../services/role.service';
import { RoleUpdate } from '../../models/role-update';

@Component({
  selector: 'app-roles',
  imports: [FormsModule],
  templateUrl: './roles.component.html',
  styleUrl: './roles.component.css'
})
export class RolesComponent {

  roles: RoleResponse[] = [];

  showForm = false;
  editingRoleId: number | null = null;
  errorMessage = "";

  newRole: RoleCreate = {
    roleName: "",
    description: ""
  };

  constructor(private readonly roleService: RoleService){
  }

  ngOnInit(): void {
    this.loadRoles();
  }

  loadRoles(): void {
    this.roleService.getRoles().subscribe({
      next: (response) => {
        this.roles = response;
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los roles"
      }
    });
  }

  createRole(): void {
    this.errorMessage = "";

    this.roleService.createRole(this.newRole).subscribe({
      next: () => {
        this.showForm = false;
        this.resetForm();
        this.loadRoles();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo crear el rol"
      }
    });
  }

  editRole(role: RoleResponse): void {
    this.editingRoleId = role.roleId;
    this.showForm = true;
    this.errorMessage = "";

    this.newRole = {
      roleName: role.roleName,
      description: role.description
    };
  }

  updateRole(): void {
    if (this.editingRoleId === null) {
      return;
    }

    const role: RoleUpdate = {
      roleName: this.newRole.roleName,
      description: this.newRole.description
    };

    this.roleService.updateRole(
      this.editingRoleId,
      role
    ).subscribe({
      next: () => {
        this.showForm = false;
        this.editingRoleId = null;
        this.resetForm();
        this.loadRoles();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo actualizar el rol"
      }
    });
  }

  saveRole(): void {
    if (this.editingRoleId === null) {
      this.createRole();
    } else {
      this.updateRole();
    }
  }

  changeStatus(role: RoleResponse): void {
    const newStatus = role.status === "ACTIVO" 
      ? "INACTIVO"
      : "ACTIVO";

    this.roleService.changeStatus(
      role.roleId,
      { status: newStatus }
    ).subscribe({
      next: () => {
        this.loadRoles();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo cambiar el estado del proveedor";
      }
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editingRoleId = null;
    this.errorMessage = "";
    this.resetForm();
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingRoleId = null;
    this.resetForm();
  }

  resetForm(): void {
    this.newRole = {
      roleName: "",
      description: ""
    };
  }
}
