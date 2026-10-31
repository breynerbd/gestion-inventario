import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserResponse } from '../../models/user-response';
import { UserCreate } from '../../models/user-create';
import { UserService } from '../../services/user.service';
import { UserUpdate } from '../../models/user-update';
import { RoleResponse } from '../../../roles/models/role-response';
import { RoleService } from '../../../roles/services/role.service';
import { PaginationComponent } from '../../../../shared/components/pagination/pagination.component';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { lucideBan, lucideCircleCheck, lucidePencil, lucidePlus } from '@ng-icons/lucide';
import { DetailField } from '../../../../shared/models/detail-field';
import { DetailModalComponent } from '../../../../shared/components/detail-modal/detail-modal.component';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-users',
  imports: [FormsModule, PaginationComponent, NgIconComponent, DetailModalComponent],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css',
  changeDetection: ChangeDetectionStrategy.Eager,
  viewProviders: [provideIcons({ lucideBan, lucideCircleCheck, lucidePencil, lucidePlus })]
})
export class UsersComponent implements OnInit {
  users: UserResponse[] = [];
  roles: RoleResponse[] = [];
  selectedUserDetails: DetailField[] = [];

  showDetails = false;
  detailTitle = "";
  showForm = false;
  editingUserId: number | null = null;
  errorMessage = "";
  errorMessageForm = "";
  searchTerm = "";

  currentPage = 1;
  pageSize = 10;

  newUser: UserCreate = {
    username: "",
    password: "",
    firstNames: "",
    lastNames: "",
    email: "",
    phone: "",
    roleId: 0
  }

  constructor(private readonly userService: UserService, private readonly roleService: RoleService) {
  }

  ngOnInit(): void {
    this.loadUsers();
    this.loadRoles();
  }

  loadUsers(): void {
    this.userService.getUsers().subscribe({
      next: (response) => {
        this.users = response;
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los usuarios"
      }
    });
  }

  loadRoles(): void {
    this.roleService.getRoles().subscribe({
      next: (response) => {
        this.roles = response.filter(role => role.status === "ACTIVO");
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los roles"
      }
    })
  }

  get filteredUsers(): UserResponse[] {
    const search = this.searchTerm.trim().toLowerCase();

    return this.users.filter(user =>
      `${user.firstNames} ${user.lastNames}`.toLowerCase().includes(search) ||
      user.username.toLowerCase().includes(search) ||
      user.email.toLowerCase().includes(search)
    );
  }

  get totalPages(): number {
    return Math.ceil(this.filteredUsers.length / this.pageSize);
  }

  get paginatedUsers(): UserResponse[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    return this.filteredUsers.slice(startIndex, startIndex + this.pageSize);
  }

  onSearchChange(value: string): void {
    this.searchTerm = value;
    this.currentPage = 1;
  }

  viewUserDetails(user: UserResponse): void {
    this.detailTitle = "Detalles del usuario";

    const role = this.roles.find(
      role => user.userId === role.roleId
    );

    this.selectedUserDetails = [
      { label: "ID", value: user.userId },
      { label: "Nombre de usuario", value: user.username },
      { label: "Nombre", value: user.firstNames + user.lastNames },
      { label: "Correo electrónico", value: user.email },
      { label: "Teléfono", value: user.phone },
      { label: "Rol", value: role?.roleName ?? "No disponible" },
      { label: "Intentos fallidos", value: user.failedAttempts},
      { label: "Fecha de creación", value: new DatePipe('en-US').transform(user.creationDate, "dd/MM/yyyy HH:mm") ?? "No disponible" },
      { label: "Última modificación", value: new DatePipe('en-US').transform(user.modificationDate, "dd/MM/yyyy HH:mm") ?? "No disponible" },
      { label: "Estado", value: user.status },
    ];

    this.showDetails = true;
  }

  closeUserDetails(): void {
    this.showDetails = false;
    this.selectedUserDetails = [];
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }

  createUser(): void {
    this.errorMessage = "";
    this.errorMessageForm = "";

    this.userService.createUser(this.newUser).subscribe({
      next: () => {
        this.showForm = false;
        this.resetForm();
        this.loadUsers();
      },
      error: (error) => {
        console.error(error);
        this.errorMessageForm = error.error?.error || "No se pudo crear el usuario";
      }
    });
  }

  editUser(user: UserResponse): void {
    this.editingUserId = user.userId;
    this.showForm = true;
    this.errorMessage = "";

    this.newUser = {
      username: user.username,
      password: "",
      firstNames: user.firstNames,
      lastNames: user.lastNames,
      email: user.email,
      phone: user.phone,
      roleId: user.roleId
    };
  }

  updateUser(): void {
    this.errorMessage = "";
    this.errorMessageForm = "";

    if (this.editingUserId === null) {
      return
    }

    const user: UserUpdate = {
      firstNames: this.newUser.firstNames,
      lastNames: this.newUser.lastNames,
      email: this.newUser.email,
      phone: this.newUser.phone,
      roleId: this.newUser.roleId
    };

    this.userService.updateUser(
      this.editingUserId,
      user
    ).subscribe({
      next: () => {
        this.showForm = false;
        this.editingUserId = null;
        this.resetForm();
        this.loadUsers();
      },
      error: (error) => {
        console.error(error);
        this.errorMessageForm = error.error?.error || "No se pudo actualizar el usuario";
      }
    });
  }


  saveUser(): void {
    this.errorMessageForm = "";

    if (
      !this.newUser.firstNames.trim() ||
      !this.newUser.lastNames.trim() ||
      !this.newUser.email.trim() ||
      this.newUser.roleId <= 0 ||
      !this.newUser.phone.trim()
    ) {
      this.errorMessageForm = "Completa todos los campos obligatorios.";
      return;
    }

    const emailPattern = /^[\w.%+-]+@[\w.-]+\.[a-zA-Z]{2,}$/;

    if (!emailPattern.test(this.newUser.email.trim())) {
      this.errorMessageForm = "Ingresa un correo electrónico válido.";
      return;
    }

    if (this.newUser.email.length > 100) {
      this.errorMessageForm = "El correo no puede superar los 100 caracteres.";
      return;
    }

    if (this.editingUserId === null) {
      if (
        !this.newUser.username.trim() ||
        !this.newUser.password
      ) {
        this.errorMessageForm = "El nombre de usuario y la contraseña son obligatorios.";
        return;
      }

      this.createUser();
    } else {
      this.updateUser();
    }
  }


  changeStatus(user: UserResponse): void {
    this.errorMessage = "";

    const newStatus = user.status === "ACTIVO"
      ? "INACTIVO"
      : "ACTIVO";

    this.userService.changeStatus(
      user.userId,
      { status: newStatus }
    ).subscribe({
      next: () => {
        this.loadUsers();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = error.error?.error || "No se pudo cambiar el estado del usuario"
      }
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editingUserId = null;
    this.errorMessage = "";
    this.errorMessageForm = "";
    this.resetForm();
  }

  onFormDialogClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.cancelForm();
    }
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingUserId = null;
    this.errorMessage = "";
    this.errorMessageForm = "";
    this.resetForm();
  }

  resetForm(): void {
    this.newUser = {
      username: "",
      password: "",
      firstNames: "",
      lastNames: "",
      email: "",
      phone: "",
      roleId: 0
    };
  }
}
