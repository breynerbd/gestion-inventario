import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild } from "@angular/core";
import { DashboardService } from '../../services/dashboard.service';
import { LowStockProduct } from '../../models/low-stock-product';
import { Chart, ChartConfiguration, registerables } from "chart.js";
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { lucidePackage, lucideTruck, lucideAlertTriangle, lucideBanknoteArrowUp, lucideBanknoteArrowDown, lucidePackageSearch, lucideShoppingCart } from '@ng-icons/lucide';
import { MovementSummary } from '../../models/movement-summary';
import { ProductsCount } from '../../models/products-category';
import { MovementResponse } from '../../../stockMovements/models/movement-response';
import { MovementService } from '../../../stockMovements/services/movement.service';
import { Router } from '@angular/router';
import { RoleAuthService } from '../../../../core/services/role-auth.service';
import { PaginationComponent } from '../../../../shared/components/pagination/pagination.component';
import { FormsModule } from '@angular/forms';
import { CategoryResponse } from '../../../categories/models/category-response';
import { CategoryService } from '../../../categories/services/category.service';
import { ProductResponse } from '../../../products/models/product-response';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard-home',
  imports: [CommonModule, NgIconComponent, PaginationComponent, FormsModule],
  templateUrl: './dashboard-home.component.html',
  styleUrl: './dashboard-home.component.css',
  viewProviders: [provideIcons({
    lucidePackage, lucideTruck, lucideAlertTriangle, lucideShoppingCart,
    lucideBanknoteArrowUp, lucideBanknoteArrowDown, lucidePackageSearch
  })]
})
export class DashboardHomeComponent implements AfterViewInit, OnDestroy {
  totalProducts = 0;
  totalSuppliers = 0;
  lowStockProducts = 0;
  inventoryValue = 0;
  inventoryInvestment = 0;
  movementsValue = 0;
  countProducts = 0;
  startDate = '';
  endDate = '';
  groupBy = 'DAY';

  @ViewChild("stockChart")
  private stockChartCanvas!: ElementRef<HTMLCanvasElement>;

  @ViewChild("movementChart")
  private movementChartCanvas!: ElementRef<HTMLCanvasElement>;

  @ViewChild("categoryChart")
  private categoryChartCanvas!: ElementRef<HTMLCanvasElement>;

  @ViewChild("productsCategoryChart")
  private productsCategoryChartCanvas!: ElementRef<HTMLCanvasElement>;

  private stockChart?: Chart;
  private movementChart?: Chart;
  private categoryChart?: Chart;
  private productsCategoryChart?: Chart;

  latestMovements: MovementResponse[] = [];
  lowStockProductsList: LowStockProduct[] = [];
  categories: CategoryResponse[] = [];
  selectedCategoryId: number | null = null;
  stockFilter = 0;
  stockLimit = 0;

  alertPageSize = 8;
  currentAlertPage = 1;

  get paginatedLowStockProducts(): LowStockProduct[] {
    const start = (this.currentAlertPage - 1) * this.alertPageSize;

    return this.lowStockProductsList.slice(
      start,
      start + this.alertPageSize
    );
  }

  get totalAlertPages(): number {
    return Math.ceil(
      this.lowStockProductsList.length / this.alertPageSize
    );
  }

  changeAlertPage(page: number): void {
    this.currentAlertPage = page;
  }

  constructor(private readonly dashboardService: DashboardService, private readonly movementService: MovementService,
    private router: Router, private readonly roleService: RoleAuthService, private readonly categoryService: CategoryService
  ) { }

  ngAfterViewInit(): void {
    this.loadLowStockProducts();
    this.loadMovementSummary();
    this.loadProductsCountCategory();

    this.loadActiveProducts();
    this.loadActiveSuppliers();
    this.loadInventoryValue();
    this.loadInventoryInvestment();
    this.loadLatestMovements();
    this.loadMovementsValue();
    this.loadProductsByMovements();
    this.loadActiveCategories();
  }

  ngOnDestroy(): void {
    this.stockChart?.destroy();
    this.movementChart?.destroy();
    this.categoryChart?.destroy();
    this.productsCategoryChart?.destroy();
  }

  showDashboardDetails(): boolean {
    return !this.roleService.isOperator();
  }

  showDashboardIndicatorsOperator(): boolean {
    return !this.roleService.isAdmin() && !this.roleService.isSupervisor();
  }

  private loadLowStockProducts(): void {
    this.dashboardService.getLowStockProducts().subscribe({
      next: (products: LowStockProduct[]) => {
        this.lowStockProductsList = products;
        this.lowStockProducts = products.length;
        this.createStockChart(products);
      },
      error: (error) => {
        console.error(error);
      }
    });
  }

  private loadActiveProducts(): void {
    this.dashboardService.getActiveProducts().subscribe({
      next: (count: number) => {
        this.totalProducts = count;
      },
      error: (error) => {
        console.error("No se pudo obtener la cantidad de productos activos", error);
      }
    });
  }

  private loadActiveSuppliers(): void {
    this.dashboardService.getActiveSuppliers().subscribe({
      next: (count: number) => {
        this.totalSuppliers = count;
      },
      error: (error) => {
        console.error("No se pudo obtener la cantidad de proveedores activos", error);
      }
    });
  }

  private loadInventoryValue(): void {
    this.dashboardService.getInventoryValue().subscribe({
      next: (value: number) => {
        this.inventoryValue = value;
      },
      error: (error) => {
        console.error("No se pudo obtener el valor del inventario", error);
      }
    });
  }

  private loadInventoryInvestment(): void {
    this.dashboardService.getInventoryInvestment().subscribe({
      next: (investment: number) => {
        this.inventoryInvestment = investment;
      },
      error: (error) => {
        console.error("No se pudo obtener la inversión del inventario", error);
      }
    })
  }

  loadLatestMovements(): void {
    this.movementService.getLatestMovements().subscribe({
      next: (response) => {
        this.latestMovements = response.content;
      },
      error: (error) => {
        console.error("Error al cargar los últimos movimientos:", error);
      }
    });
  }

  private loadMovementsValue(): void {
    this.dashboardService.getMovementsValue().subscribe({
      next: (value: number) => {
        this.movementsValue = value;
      },
      error: (error) => {
        console.error("No se pudo obtener la ganancia de los movimientos", error);
      }
    });
  }

  private loadProductsByMovements(): void {
    this.dashboardService.getProductsByMovements().subscribe({
      next: (sumProducts: number) => {
        this.countProducts = sumProducts;
      },
      error: (error) => {
        console.error("No se pudo obtener la suma de productos", error);
      }
    });
  }

  viewAllMovements(): void {
    this.router.navigate(["/dashboard/movements"]);
  }

  private createStockChart(products: LowStockProduct[]): void {
    const canvas = this.stockChartCanvas?.nativeElement;

    if (!canvas) {
      return;
    }

    this.stockChart?.destroy();

    let filteredProducts = products;

    if (this.stockFilter === 1) {
        filteredProducts = products.filter(
            product => product.currentStock === 0
        );
    } else if (this.stockFilter === 2) {
        filteredProducts = products.filter(
            product => product.currentStock > 0
        );
    }

    const sortedProducts = [...filteredProducts].sort(
      (a, b) => a.currentStock - b.currentStock);

    const displayedProducts = this.stockLimit > 0
      ? sortedProducts.slice(0, this.stockLimit)
      : sortedProducts;

    const configuration: ChartConfiguration<"bar"> = {
      type: "bar",
      data: {
        labels: displayedProducts.map(product => product.productName),
        datasets: [
          {
            label: "Stock actual",
            data: displayedProducts.map(product => product.currentStock),
            backgroundColor: "#709eb7",
            borderRadius: 5
          },
          {
            label: "Stock mínimo",
            data: displayedProducts.map(product => product.minimumStock),
            backgroundColor: "#B8DBD9",
            borderRadius: 5
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            title: {
              display: true,
              text: "Cantidad de unidades"
            }
          }
        },
        plugins: {
          legend: {
            position: "bottom"
          }
        }
      }
    };

    this.stockChart = new Chart(canvas, configuration);
  }

  onStockFilterChange(): void {
    this.createStockChart(this.lowStockProductsList);
  }

  private loadMovementSummary(): void {
    this.dashboardService.getMovementsSummary(
      this.startDate || undefined,
      this.endDate || undefined,
      this.groupBy
    ).subscribe({
      next: (movements: MovementSummary[]) => {
        this.createMovementChart(movements);
      },
      error: (error) => {
        console.error("Error al cargar el resumen de movimientos", error);
      }
    });
  }

  applyMovementFilters(): void {
    if (
      this.startDate &&
      this.endDate &&
      this.startDate > this.endDate
    ) {
      return;
    }

    this.loadMovementSummary();
  }

  private createMovementChart(movements: MovementSummary[]): void {
    const canvas = this.movementChartCanvas?.nativeElement;

    if (!canvas) {
      return;
    }

    this.movementChart?.destroy();

    this.movementChart = new Chart(canvas, {
      type: 'line',
      data: {
        labels: movements.map(movement => movement.date),
        datasets: [
          {
            label: 'Entradas',
            data: movements.map(movement => movement.entries),
            borderColor: '#709eb7',
            backgroundColor: '#709eb7',
            tension: 0.3,
            fill: false
          },
          {
            label: 'Salidas',
            data: movements.map(movement => movement.exits),
            borderColor: '#B8DBD9',
            backgroundColor: '#B8DBD9',
            tension: 0.3,
            fill: false
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            title: {
              display: true,
            }
          },
          y: {
            beginAtZero: true,
            ticks: {
              precision: 0
            },
            title: {
              display: true,
              text: 'Cantidad de movimientos'
            }
          }
        },
        plugins: {
          legend: {
            position: 'bottom'
          },
          tooltip: {
            mode: 'index',
            intersect: false
          }
        }
      }
    });
  }


  private loadProductsCountCategory(): void {
    this.dashboardService.getProductsCount().subscribe({
      next: (categories: ProductsCount[]) => {
        this.createCategoryChart(categories);
      },
      error: (error) => {
        console.error("Error al cargar la agrupacion por categoría", error);
      }
    });
  }

  private createCategoryChart(categories: ProductsCount[]): void {
    const canvas = this.categoryChartCanvas?.nativeElement;

    if (!canvas) {
      return;
    }

    this.categoryChart?.destroy();

    this.categoryChart = new Chart(canvas, {
      type: "pie",
      data: {
        labels: categories.map(category => category.categoryName),
        datasets: [
          {
            label: "Productos",
            data: categories.map(category => category.productCount),
            backgroundColor: [
              "#709eb7",
              "#B8DBD9",
              "#586F7C",
              "#F4B860",
              "#E07A5F",
              "#81B29A",
              "#9B8EC4",
              "#E6A4B4"
            ],
            borderWidth: 2,
            borderColor: "#FFFFFF"
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: "bottom"
          }
        }
      }
    });
  }

  private loadActiveCategories(): void {
    this.categoryService.getCategories(0, 100).subscribe({
      next: (response) => {
        this.categories = response.content.filter(
          category => category.status === "ACTIVO"
        );

        this.loadCategoriesWithStock();
      },
      error: (error) => {
        console.error("No se pudieron cargar las categorías", error);
      }
    });
  }

  private loadCategoriesWithStock(): void {
    this.dashboardService.getProductsCount().subscribe({
      next: (categoriesWithProducts: ProductsCount[]) => {
        const categoryNames = categoriesWithProducts.map(
          category => category.categoryName
        );

        this.categories = this.categories.filter(
          category => categoryNames.includes(category.categoryName)
        );

        if (this.categories.length > 0) {
          this.selectedCategoryId = this.categories[0].categoryId;
          this.loadProductsByCategory();
        } else {
          this.selectedCategoryId = null;
          this.productsCategoryChart?.destroy();
          this.productsCategoryChart = undefined;
        }
      },
      error: (error) => {
        console.error("No se pudieron cargar las categorías con stock", error);
      }
    });
  }

  onCategoryChange(): void {
    this.loadProductsByCategory();
  }

  private loadProductsByCategory(): void {
    if (this.selectedCategoryId === null) {
      this.productsCategoryChart?.destroy();
      this.productsCategoryChart = undefined;
      return;
    }

    this.dashboardService.getProductsByCategory(this.selectedCategoryId).subscribe({
      next: (response) => {
        const productsWithStock = response.content.filter(
          product => product.currentStock > 0
        );
        this.createProductsCategoryChart(productsWithStock);
      },
      error: (error) => {
        console.error("No se pudieron cargar los productos de la categoría", error);
      }
    });
  }

  private createProductsCategoryChart(products: ProductResponse[]): void {
    const canvas = this.productsCategoryChartCanvas?.nativeElement;

    if (!canvas) {
      return;
    }

    this.productsCategoryChart?.destroy();

    this.productsCategoryChart = new Chart(canvas, {
      type: "pie",
      data: {
        labels: products.map(product => product.productName),
        datasets: [
          {
            label: "Stock actual",
            data: products.map(product => product.currentStock),
            backgroundColor: [
              "#709eb7",
              "#B8DBD9",
              "#586F7C",
              "#F4B860",
              "#E07A5F",
              "#81B29A",
              "#9B8EC4",
              "#E6A4B4"
            ],
            borderWidth: 2,
            borderColor: "#FFFFFF"
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: "bottom"
          },
          tooltip: {
            callbacks: {
              label: (context) =>
                ` ${context.parsed} unidades`
            }
          }
        }
      }
    });
  }
}
