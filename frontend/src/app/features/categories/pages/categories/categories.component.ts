import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CategoryResponse } from '../../models/category-response';
import { CategoryCreate } from '../../models/category-create';
import { CategoryUpdate } from '../../models/category-update';
import { CategoryService } from '../../services/category.service';
import { RoleAuthService } from '../../../../core/services/role-auth.service';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { lucideBan, lucideCircleCheck, lucidePencil, lucidePlus, lucideChevronRight, lucideChevronLeft } from '@ng-icons/lucide';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-categories',
  imports: [FormsModule, NgIconComponent, DatePipe],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css',
  changeDetection: ChangeDetectionStrategy.Eager,
  viewProviders: [provideIcons({ lucideBan, lucideCircleCheck, lucidePencil, lucidePlus, lucideChevronRight, lucideChevronLeft })]
})
export class CategoriesComponent implements OnInit {
  categories: CategoryResponse[] = [];

  showForm = false;
  editingCategoryId: number | null = null;
  errorMessage = "";
  errorMessageForm = "";

  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;
  searchTerm = "";

  newCategory: CategoryCreate = {
    categoryName: "",
    description: ""
  };

  constructor(private readonly categoryService: CategoryService, private readonly roleAuthService: RoleAuthService) {

  }

  canManageCategories(): boolean {
    return this.roleAuthService.createCategory();
  }

  ngOnInit(): void {
    this.loadCategories();
  }

  editCategory(category: CategoryResponse): void {
    this.editingCategoryId = category.categoryId;
    this.showForm = true;
    this.errorMessage = "";

    this.newCategory = {
      categoryName: category.categoryName,
      description: category.description || ""
    };
  }

  loadCategories(): void {
    this.categoryService.getCategories(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.categories = response.content;
        this.totalPages = response.totalPages;
        this.totalElements = response.totalElements;
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar las categorias"
      }
    });
  }

  get filteredCategories(): CategoryResponse[] {
    const search = this.searchTerm.trim().toLowerCase();

    return this.categories.filter(categorie => 
      categorie.categoryCode.toLowerCase().includes(search) ||
      categorie.categoryName.toLowerCase().includes(search)
    );
  }

  onSearchChange(value: string): void {
    this.searchTerm = value;
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadCategories();
    }
  }

  createCategory(): void {
    this.errorMessage = "";
    this.errorMessageForm = "";

    this.categoryService.createCategory(this.newCategory).subscribe({
      next: () => {
        this.showForm = false;
        this.resetForm();
        this.loadCategories();
      },
      error: (error) => {
        console.error(error);
        this.errorMessageForm = error.error?.error || "No se pudo crear la categoria";
      }
    });
  }

  updateCategory(): void {
    this.errorMessage = "";
    this.errorMessageForm = "";

    if (this.editingCategoryId === null) {
      return;
    }

    const category: CategoryUpdate = {
      categoryName: this.newCategory.categoryName,
      description: this.newCategory.description
    };

    this.categoryService.updateCategory(
      this.editingCategoryId,
      category
    ).subscribe({
      next: () => {
        this.showForm = false;
        this.editingCategoryId = null;
        this.resetForm();
        this.loadCategories();
      },
      error: (error) => {
        console.error(error);
        this.errorMessageForm = error.error?.error || "No se pudo actualizar la categoria";
      }
    });
  }

  saveCategory(): void {
    this.errorMessageForm = "";

    if (!this.newCategory.categoryName.trim()) {
      this.errorMessageForm = "El nombre de la categoría es obligatorio.";
      return;
    }

    if (this.editingCategoryId === null) {
      this.createCategory();
    } else {
      this.updateCategory();
    }
  }

  changeStatus(category: CategoryResponse): void {
    const newStatus = category.status === "ACTIVO"
      ? "INACTIVO"
      : "ACTIVO";

    this.categoryService.changeStatus(
      category.categoryId,
      { status: newStatus }
    ).subscribe({
      next: () => {
        this.errorMessage = "";
        this.loadCategories();
      },
      error: (error) => {
        console.error(error);
        
        const message = error.error?.error;

        if (message?.includes("productos asociados")) {
          this.errorMessage = message;
        } else {
          this.errorMessage = message || "No se pudo cambiar el estado de la categoria"
        }
      }
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editingCategoryId = null;
    this.errorMessage = "";
    this.errorMessageForm = "";
    this.resetForm();
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingCategoryId = null;
    this.errorMessage = "";
    this.errorMessageForm = "";
    this.resetForm();
  }

  resetForm(): void {
    this.newCategory = {
      categoryName: "",
      description: ""
    };
  }

}
