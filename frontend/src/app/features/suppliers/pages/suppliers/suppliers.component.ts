import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";

import { SupplierResponse } from "../../models/supplier-response";
import { SupplierCreate } from "../../models/supplier-create";
import { SupplierUpdate } from "../../models/supplier-update";
import { SupplierService } from "../../service/supplier.service";
import { RoleAuthService } from "../../../../core/services/role-auth.service";
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { lucideBan, lucideCircleCheck, lucidePencil, lucidePlus } from '@ng-icons/lucide';
import { DetailField } from "../../../../shared/models/detail-field";
import { DetailModalComponent } from "../../../../shared/components/detail-modal/detail-modal.component";

@Component({
  selector: "app-suppliers",
  imports: [FormsModule, NgIconComponent, DetailModalComponent],
  templateUrl: "./suppliers.component.html",
  styleUrl: "./suppliers.component.css",
  viewProviders: [provideIcons({ lucideBan, lucideCircleCheck, lucidePencil, lucidePlus })]
})
export class SuppliersComponent implements OnInit {

  suppliers: SupplierResponse[] = [];
  selectedSupplierDetails: DetailField[] = [];

  showDetails = false;
  detailTitle = "";
  showForm = false;
  editingSupplierId: number | null = null;
  errorMessage = "";
  errorMessageForm = "";
  searchTerm = "";

  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;

  newSupplier: SupplierCreate = {
    supplierCode: "",
    documentType: "",
    documentNumber: "",
    businessName: "",
    contactName: "",
    phone: "",
    email: "",
    address: ""
  };

  constructor(private readonly supplierService: SupplierService, private readonly roleAuthService: RoleAuthService) {

  }

  ngOnInit(): void {
    this.loadSuppliers();
  }

  canManageSuppliers(): boolean {
    return this.roleAuthService.createSupplier();
  }

  loadSuppliers(): void {
    this.supplierService.getSuppliers(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.suppliers = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los proveedores";
      }
    });
  }

  get filteredSuppliers(): SupplierResponse[] {
    const search = this.searchTerm.trim().toLowerCase();

    return this.suppliers.filter(supplier => 
      supplier.supplierCode.toLowerCase().includes(search) ||
      supplier.businessName.toLowerCase().includes(search) ||
      supplier.contactName.toLowerCase().includes(search) ||
      supplier.email.toLowerCase().includes(search)
    );
  }

  onSearchChange(value: string): void {
    this.searchTerm = value;
  }

  viewSupplierDetails(supplier: SupplierResponse): void {
    this.detailTitle = "Detalles del proveedor";

    this.selectedSupplierDetails = [
      { label: "ID", value: supplier.supplierId },
      { label: "Código", value: supplier.supplierCode },
      { label: "Razón social", value: supplier.businessName },
      { label: "Tipo de documento", value: supplier.documentType },
      { label: "Número de documento", value: supplier.documentNumber },
      { label: "Nombre de contacto", value: supplier.contactName },
      { label: "Teléfono", value: supplier.phone },
      { label: "Correo electrónico", value: supplier.email },
      { label: "Dirección", value: supplier.address }
    ];

    this.showDetails = true;
  }

  closeSupplierDetails(): void {
    this.showDetails = false;
    this.selectedSupplierDetails = [];
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadSuppliers();
    }
  }

  createSupplier(): void {
    this.errorMessage = "";

    this.supplierService.createSupplier(this.newSupplier).subscribe({
      next: () => {
        this.showForm = false;
        this.resetForm();
        this.loadSuppliers();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo crear el proveedor";
      }
    });
  }

  editSupplier(supplier: SupplierResponse): void {
    this.editingSupplierId = supplier.supplierId;
    this.showForm = true;
    this.errorMessage = "";

    this.newSupplier = {
      supplierCode: supplier.supplierCode,
      documentType: supplier.documentType,
      documentNumber: supplier.documentNumber,
      businessName: supplier.businessName,
      contactName: supplier.contactName,
      phone: supplier.phone,
      email: supplier.email,
      address: supplier.address
    };
  }

  updateSupplier(): void {
    if (this.editingSupplierId === null) {
      return;
    }

    const supplier: SupplierUpdate = {
      documentType: this.newSupplier.documentType,
      documentNumber: this.newSupplier.documentNumber,
      businessName: this.newSupplier.businessName,
      contactName: this.newSupplier.contactName,
      phone: this.newSupplier.phone,
      email: this.newSupplier.email,
      address: this.newSupplier.address
    };

    this.supplierService.updateSupplier(
      this.editingSupplierId,
      supplier
    ).subscribe({
      next: () => {
        this.showForm = false;
        this.editingSupplierId = null;
        this.resetForm();
        this.loadSuppliers();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo actualizar el proveedor";
      }
    });
  }


  saveSupplier(): void {
    this.errorMessageForm = "";

    if (
      !this.newSupplier.documentNumber.trim() ||
      !this.newSupplier.businessName.trim() ||
      !this.newSupplier.phone.trim() ||
      !this.newSupplier.email.trim() ||
      !this.newSupplier.address.trim()
    ) {
      this.errorMessageForm = "Completa todos los campos obligatorios.";
      return;
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!emailPattern.test(this.newSupplier.email.trim())) {
      this.errorMessageForm = "Ingresa un correo electrónico válido.";
      return;
    }

    if (this.newSupplier.email.length > 100) {
      this.errorMessageForm = "El correo no puede superar los 100 caracteres.";
      return;
    }

    if (this.editingSupplierId === null) {
      this.createSupplier();
    } else {
      this.updateSupplier();
    }
  }


  changeStatus(supplier: SupplierResponse): void {
    const newStatus = supplier.status === "ACTIVO"
      ? "INACTIVO"
      : "ACTIVO";

    this.supplierService.changeStatus(
      supplier.supplierId,
      { status: newStatus }
    ).subscribe({
      next: () => {
        this.errorMessage = "";
        this.loadSuppliers();
      },
      error: (error) => {
        console.error(error);
        
        const message = error.error?.error;

        if (message?.includes("productos asociados")) {
          this.errorMessage = message;
        } else {
          this.errorMessage = message || "No se pudo cambiar el estado del proveedor";
        }
      }
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editingSupplierId = null;
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
    this.editingSupplierId = null;
    this.resetForm();
  }

  resetForm(): void {
    this.newSupplier = {
      supplierCode: "",
      documentType: "",
      documentNumber: "",
      businessName: "",
      contactName: "",
      phone: "",
      email: "",
      address: ""
    };
  }
}