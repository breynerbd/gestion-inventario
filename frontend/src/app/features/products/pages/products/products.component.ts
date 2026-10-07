import { Component, OnInit } from '@angular/core';
import { ProductResponse } from '../../models/product-response';
import { ProductService } from '../../services/product.service';
import { SupplierResponse } from '../../../suppliers/models/supplier-response';
import { CategoryResponse } from '../../../categories/models/category-response';
import { CategoryService } from '../../../categories/services/category.service';
import { SupplierService } from '../../../suppliers/service/supplier.service';
import { FormsModule } from '@angular/forms';
import { ProductCreate } from '../../models/product-create';
import { ProductUpdate } from '../../models/product-update';

@Component({
  selector: 'app-products',
  imports: [FormsModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent implements OnInit {
  products: ProductResponse[] = [];
  categories: CategoryResponse[] = [];
  suppliers: SupplierResponse[] = [];

  showForm = false;
  editingProductId: number | null = null;
  errorMessage = "";

  searchProductCode = "";
  searchProductName = "";
  selectedCategoryId = 0;
  selectedSupplierId = 0;
  selectedStatus = "";

  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;

  newProduct: ProductCreate = {
    productCode: '',
    productName: '',
    description: '',
    categoryId: 0,
    supplierId: 0,
    unitOfMeasure: '',
    purchasePrice: 0,
    salePrice: 0,
    minimumStock: 0,
    maximumStock: 0
  };

  editProduct(product: ProductResponse): void {
    this.editingProductId = product.productId;
    this.showForm = true;
    this.errorMessage = '';

    this.newProduct = {
      productCode: product.productCode,
      productName: product.productName,
      description: product.description || '',
      categoryId: product.categoryId,
      supplierId: product.supplierId,
      unitOfMeasure: product.unitOfMeasure,
      purchasePrice: product.purchasePrice,
      salePrice: product.salePrice,
      minimumStock: product.minimumStock,
      maximumStock: product.maximumStock || 0
    };
  }

  constructor(private readonly productService: ProductService, private readonly categoryService: CategoryService,
    private readonly supplierService: SupplierService
  ) {
  }

  ngOnInit(): void {
    this.loadProducts();
    this.loadCategories();
    this.loadSuppliers();
  }

  loadProducts(): void {
    this.productService.getProducts(this.currentPage, this.pageSize, this.searchProductCode, this.searchProductName, 
      this.selectedCategoryId, this.selectedSupplierId, this.selectedStatus)
    .subscribe({
      next: (response) => {
        this.products = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los productos"
      }
    });
  }

  applyFilters(): void {
    this.currentPage = 0;
    this.loadProducts();
  }

  clearFilters(): void {
    this.searchProductCode = "";
    this.searchProductName = "";
    this.selectedCategoryId = 0;
    this.selectedSupplierId = 0;
    this.selectedStatus = "";

    this.currentPage = 0;
    this.loadProducts();
  }

  loadCategories(): void {
    this.categoryService.getCategories().subscribe({
      next: (response) => {
        this.categories = response.content.filter(category => category.status === "ACTIVO");
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar las categorías";
      }
    });
  }

  loadSuppliers(): void {
    this.supplierService.getSuppliers().subscribe({
      next: (response) => {
        this.suppliers = response.content.filter(supplier => supplier.status === "ACTIVO");
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los proveedores";
      }
    });
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadProducts();
    }
  }

  createProduct(): void {
    this.errorMessage = '';

    this.productService.createProduct(this.newProduct).subscribe({
      next: () => {
        this.showForm = false;
        this.resetForm();
        this.loadProducts();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = 'No se pudo crear el producto.';
      }
    });
  }

  updateProduct(): void {
    if (this.editingProductId === null) {
      return;
    }

    const product: ProductUpdate = {
      productName: this.newProduct.productName,
      description: this.newProduct.description,
      categoryId: this.newProduct.categoryId,
      supplierId: this.newProduct.supplierId,
      unitOfMeasure: this.newProduct.unitOfMeasure,
      purchasePrice: this.newProduct.purchasePrice,
      salePrice: this.newProduct.salePrice,
      minimumStock: this.newProduct.minimumStock,
      maximumStock: this.newProduct.maximumStock
    };

    this.productService.updateProduct(
      this.editingProductId,
      product
    ).subscribe({
      next: () => {
        this.showForm = false;
        this.editingProductId = null;
        this.resetForm();
        this.loadProducts();
      },
      error: (error) => {
        console.log(error);
        this.errorMessage = "No se pudo actualizar el producto"
      }
    });
  }

  saveProduct(): void {
    this.errorMessage = "";

    if (this.newProduct.categoryId === 0 || this.newProduct.supplierId === 0 || this.newProduct.unitOfMeasure === "") {
      this.errorMessage = "Completa la informacion del producto";
      return;
    }

    if (this.editingProductId === null) {
      this.createProduct();
    } else {
      this.updateProduct();
    }
  }

  changeStatus(product: ProductResponse): void {
    const newStatus = product.status == "ACTIVO"
      ? "INACTIVO"
      : "ACTIVO";

    this.productService.changeStatus(
      product.productId,
      { status: newStatus }
    ).subscribe({
      next: () => {
        this.loadProducts();
      },
      error: (error) => {
        console.log(error);
        this.errorMessage = "No se pudo cambiar el estado del producto"
      }
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editingProductId = null;
    this.errorMessage = '';
    this.resetForm();
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingProductId = null;
    this.resetForm();
  }

  resetForm(): void {
    this.newProduct = {
      productCode: '',
      productName: '',
      description: '',
      categoryId: 0,
      supplierId: 0,
      unitOfMeasure: '',
      purchasePrice: 0,
      salePrice: 0,
      minimumStock: 0,
      maximumStock: 0
    };
  }
}
