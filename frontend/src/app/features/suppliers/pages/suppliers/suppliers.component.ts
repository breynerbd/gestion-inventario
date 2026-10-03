import { Component, OnInit } from "@angular/core";
import { FormsModule } from "@angular/forms";

import { SupplierResponse } from "../../models/supplier-response";
import { SupplierCreate } from "../../models/supplier-create";
import { SupplierUpdate } from "../../models/supplier-update";
import { SupplierService } from "../../service/supplier.service";

@Component({
  selector: "app-suppliers",
  imports: [FormsModule],
  templateUrl: "./suppliers.component.html",
  styleUrl: "./suppliers.component.css"
})
export class SuppliersComponent implements OnInit {

  suppliers: SupplierResponse[] = [];

  showForm = false;
  editingSupplierId: number | null = null;
  errorMessage = "";

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

  constructor(private readonly supplierService: SupplierService) { 

  }

  ngOnInit(): void {
    this.loadSuppliers();
  }

  loadSuppliers(): void {
    this.supplierService.getSuppliers().subscribe({
      next: (response) => {
        this.suppliers = response.content;
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los proveedores";
      }
    });
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
    this.errorMessage = "";

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
        this.loadSuppliers();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo cambiar el estado del proveedor";
      }
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editingSupplierId = null;
    this.errorMessage = "";
    this.resetForm();
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