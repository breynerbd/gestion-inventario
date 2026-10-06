import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CategoryResponse } from '../../models/category-response';
import { CategoryCreate } from '../../models/category-create';
import { CategoryUpdate } from '../../models/category-update';
import { CategoryService } from '../../services/category.service';

@Component({
  selector: 'app-categories',
  imports: [FormsModule],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.css'
})
export class CategoriesComponent implements OnInit {
  categories: CategoryResponse[] = [];

  showForm = false;
  editingCategoryId: number | null = null;
  errorMessage = "";

  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalElements: number = 0;

  newCategory: CategoryCreate = {
    categoryCode: "",
    categoryName: "",
    description: ""
  };

  constructor(private readonly categoryService: CategoryService) {

  }

  ngOnInit(): void {
    this.loadCategories();
  }

  editCategory(category: CategoryResponse): void {
    this.editingCategoryId = category.categoryId;
    this.showForm = true;
    this.errorMessage = "";

    this.newCategory = {
      categoryCode: category.categoryCode,
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

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadCategories();
    }
  }

  createCategory(): void {
    this.errorMessage = "";

    this.categoryService.createCategory(this.newCategory).subscribe({
      next: () => {
        this.showForm = false;
        this.resetForm();
        this.loadCategories();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo crear la categoria"
      }
    });
  }

  updateCategory(): void {
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
        this.errorMessage = "No se pudo actualizar la categoria"
      }
    });
  }

  saveCategory(): void {
    this.errorMessage = "";

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
        this.loadCategories();
      },
      error: (error) => {
        console.error(error);
        this.errorMessage = "No se pudo cambiar el estado de la categoria"
      }
    });
  }

  openForm(): void {
    this.showForm = true;
    this.editingCategoryId = null;
    this.errorMessage = "";
    this.resetForm();
  }

  cancelForm(): void {
    this.showForm = false;
    this.editingCategoryId = null;
    this.resetForm();
  }

  resetForm(): void {
    this.newCategory = {
      categoryCode: "",
      categoryName: "",
      description: ""
    };
  }

}
