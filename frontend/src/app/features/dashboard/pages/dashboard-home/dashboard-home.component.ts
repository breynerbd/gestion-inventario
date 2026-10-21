import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild } from "@angular/core";
import { DashboardService } from '../../services/dashboard.service';
import { LowStockProduct } from '../../models/low-stock-product';
import { Chart, ChartConfiguration, registerables } from "chart.js";
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { lucidePackage, lucideTruck, lucideAlertTriangle, lucideBanknoteArrowUp, lucideBanknoteArrowDown } from '@ng-icons/lucide';
import { MovementSummary } from '../../models/movement-summary';
import { ProductsCount } from '../../models/products-category';
import { MovementResponse } from '../../../stockMovements/models/movement-response';
import { MovementService } from '../../../stockMovements/services/movement.service';
import { Router } from '@angular/router';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard-home',
  imports: [CommonModule, NgIconComponent],
  templateUrl: './dashboard-home.component.html',
  styleUrl: './dashboard-home.component.css',
  viewProviders: [provideIcons({ lucidePackage, lucideTruck, lucideAlertTriangle, lucideBanknoteArrowUp, lucideBanknoteArrowDown })]
})
export class DashboardHomeComponent implements AfterViewInit, OnDestroy {
  totalProducts = 0;
  totalSuppliers = 0;
  lowStockProducts = 0;
  inventoryValue = 0;
  inventoryInvestment = 0;

  @ViewChild("stockChart")
  private stockChartCanvas!: ElementRef<HTMLCanvasElement>;

  @ViewChild("movementChart")
  private movementChartCanvas!: ElementRef<HTMLCanvasElement>;

  @ViewChild("categoryChart")
  private categoryChartCanvas!: ElementRef<HTMLCanvasElement>;

  private stockChart?: Chart;
  private movementChart?: Chart;
  private categoryChart?: Chart;

  latestMovements: MovementResponse[] = [];

  constructor(private readonly dashboardService: DashboardService, private readonly movementService: MovementService,
    private router: Router
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
  }

  ngOnDestroy(): void {
    this.stockChart?.destroy();
    this.movementChart?.destroy();
    this.categoryChart?.destroy();
  }

  private loadLowStockProducts(): void {
    this.dashboardService.getLowStockProducts().subscribe({
      next: (products: LowStockProduct[]) => {
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

  viewAllMovements(): void {
    this.router.navigate(["/dashboard/movements"]);
  }

  private createStockChart(products: LowStockProduct[]): void {
    const canvas = this.stockChartCanvas?.nativeElement;

    if (!canvas) {
      return;
    }

    this.stockChart?.destroy();

    const configuration: ChartConfiguration<"bar"> = {
      type: "bar",
      data: {
        labels: products.map(product => product.productName),
        datasets: [
          {
            label: "Stock actual",
            data: products.map(product => product.currentStock),
            backgroundColor: "#709eb7",
            borderRadius: 5
          },
          {
            label: "Stock mínimo",
            data: products.map(product => product.minimumStock),
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

  private loadMovementSummary(): void {
    this.dashboardService.getMovementsSummary().subscribe({
      next: (movements: MovementSummary[]) => {
        this.createMovementChart(movements);
      },
      error: (error) => {
        console.error("Error al cargar el resumen de movimientos", error);
      }
    });
  }

  private createMovementChart(movements: MovementSummary[]): void {
    const context = this.movementChartCanvas.nativeElement.getContext("2d");

    if (!context) {
      return;
    }

    this.movementChart?.destroy();

    this.movementChart = new Chart(context, {
      type: "line",
      data: {
        labels: movements.map(movement => movement.date),
        datasets: [
          {
            label: "Entradas",
            data: movements.map(movement => movement.entries),
            borderColor: "#709eb7",
            backgroundColor: "#709eb7",
            tension: 0.3,
            fill: false
          },
          {
            label: "Salidas",
            data: movements.map(movement => movement.exits),
            borderColor: "#B8DBD9",
            backgroundColor: "#B8DBD9",
            tension: 0.3,
            fill: false
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              precision: 0
            }
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
}
