import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";

import { MovementCreate } from "../../models/movement-create";
import { MovementResponse } from "../../models/movement-response";
import { MovementUpdate } from "../../models/movement-update";
import { MovementService } from "../../services/movement.service";

import { ProductResponse } from "../../../products/models/product-response";
import { ProductService } from "../../../products/services/product.service";
import { UserResponse } from "../../../users/models/user-response";
import { UserService } from "../../../users/services/user.service";
import { DatePipe } from "@angular/common";
import { RoleAuthService } from "../../../../core/services/role-auth.service";
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { lucideBan, lucideCircleCheck, lucidePencilSparkles, lucidePlus, lucideChevronLeft, lucideChevronRight } from '@ng-icons/lucide';

@Component({
  selector: "app-movements",
  imports: [FormsModule, DatePipe, NgIconComponent],
  templateUrl: "./movements.component.html",
  styleUrl: "./movements.component.css",
  viewProviders: [provideIcons({ lucideBan, lucideCircleCheck, lucidePencilSparkles, lucidePlus, lucideChevronLeft, lucideChevronRight })]
})
export class MovementsComponent implements OnInit {
  movements: MovementResponse[] = [];
  products: ProductResponse[] = [];
  users: UserResponse[] = [];

  showForm = false;
  editingMovementId: number | null = null;
  errorMessage = "";
  errorMessageForm = "";

  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;

  selectedProductId: number = 0;
  selectedMovementType = "";
  startDate = "";
  endDate = "";
  selectedUserId: number = 0;

  newMovement: MovementCreate = {
    movementType: "",
    productId: 0,
    quantity: 0,
    referenceDocument: "",
    reason: ""
  };

  constructor(private readonly movementService: MovementService, private readonly productService: ProductService,
    private readonly userService: UserService, private readonly roleAuthService: RoleAuthService
  ) {
  }

  ngOnInit(): void {
    this.loadMovements();
    this.loadProducts();
    this.loadUsers();
  }

  isAdmin(): boolean {
    return this.roleAuthService.isAdmin();
  }

  canCreateMovements(): boolean {
    return this.roleAuthService.createMovement();
  }

  canEditMovements(): boolean {
    return this.roleAuthService.editMovement();
  }

  loadMovements(): void {
    this.movementService.getMovements(this.currentPage, this.pageSize, this.selectedProductId,
      this.selectedMovementType, this.startDate, this.endDate, this.selectedUserId
    ).subscribe({
      next: (response) => {
        this.movements = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los movimientos";
      }
    });
  }

  loadProducts(): void {
    this.productService.getProducts(0, 100).subscribe({
      next: (response) => {
        this.products = response.content.filter(product => product.status === "ACTIVO");
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los productos";
      }
    });
  }

  loadUsers(): void {
    if(!this.isAdmin()) {
      return;
    }

    this.userService.getUsers().subscribe({
      next: (response) => {
        this.users = response;
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los usuarios";
      }
    });
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadMovements();
    }
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.loadMovements();
  }

  clearFilters(): void {
    this.selectedProductId = 0;
    this.selectedMovementType = "";
    this.startDate = "";
    this.endDate = "";
    this.selectedUserId = 0;
    this.currentPage = 0;
    this.loadMovements();
  }

  createMovement(): void {
    this.errorMessage = "";

    this.movementService.createMovement(this.newMovement).subscribe({
      next: () => {
        this.showForm = false;
        this.resetForm();
        this.loadMovements();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo crear el movimiento";
      }
    });
  }

  editMovement(movement: MovementResponse): void {
    this.editingMovementId = movement.movementId;
    this.showForm = true;
    this.errorMessage = "";

    this.newMovement = {
      movementType: movement.movementType,
      productId: movement.productId,
      quantity: movement.quantity,
      referenceDocument: movement.referenceDocument,
      reason: movement.reason
    };
  }

  updateMovement(): void {
    if (this.editingMovementId === null) {
      return;
    }

    const movement: MovementUpdate = {
      movementType: this.newMovement.movementType,
      productId: this.newMovement.productId,
      quantity: this.newMovement.quantity,
      referenceDocument: this.newMovement.referenceDocument,
      reason: this.newMovement.reason
    };

    this.movementService.updateMovement(
      this.editingMovementId,
      movement
    ).subscribe({
      next: () => {
        this.showForm = false;
        this.editingMovementId = null;
        this.resetForm();
        this.loadMovements();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo actualizar el movimiento";
      }
    });
  }

  saveMovement(): void {
    this.errorMessageForm = "";

    if (this.newMovement.movementType === "" || this.newMovement.productId === 0
    ) {
      this.errorMessageForm = "Selecciona el tipo de movimiento y el producto.";
      return;
    }

    if (this.newMovement.quantity === null || this.newMovement.quantity === undefined ||
      !Number.isInteger(this.newMovement.quantity) || this.newMovement.quantity < 1
    ) {
      this.errorMessageForm = "La cantidad debe ser un número entero mayor que cero.";
      return;
    }

    if (this.newMovement.referenceDocument && this.newMovement.referenceDocument.length > 30
    ) {
      this.errorMessageForm = "El documento de referencia tiene un máximo de 30 caracteres.";
      return;
    }

    if (this.newMovement.reason && this.newMovement.reason.length > 250
    ) {
      this.errorMessageForm = "El motivo tiene un máximo de 250 caracteres.";
      return;
    }

    if (this.editingMovementId === null) {
      this.createMovement();
    } else {
      this.updateMovement();
    }

    if (this.editingMovementId === null) {
      this.createMovement();
    } else {
      this.updateMovement();
    }
  }

  changeStatus(movement: MovementResponse): void {
    const newStatus = movement.status === "ACTIVO"
      ? "INACTIVO"
      : "ACTIVO";

    this.movementService.changeStatus(
      movement.movementId,
      { status: newStatus }
    ).subscribe({
      next: () => {
        this.loadMovements();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo cambiar el estado del movimiento";
      }
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editingMovementId = null;
    this.errorMessage = "";
    this.errorMessageForm = "";
    this.resetForm();
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingMovementId = null;
    this.resetForm();
  }

  resetForm(): void {
    this.newMovement = {
      movementType: "",
      productId: 0,
      quantity: 0,
      referenceDocument: "",
      reason: ""
    };
  }
}