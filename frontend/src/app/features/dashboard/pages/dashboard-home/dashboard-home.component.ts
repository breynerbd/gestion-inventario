import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild } from "@angular/core";
import { DashboardService } from '../../services/dashboard.service';
import { LowStockProduct } from '../../models/low-stock-product';
import { Chart, ChartConfiguration, registerables } from "chart.js";
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { lucidePackage, lucideTruck, lucideAlertTriangle, lucideBanknoteArrowUp } from '@ng-icons/lucide';
import { MovementSummary } from '../../models/movement-summary';

Chart.register(...registerables);

@Component({
  selector: 'app-dashboard-home',
  imports: [CommonModule, NgIconComponent],
  templateUrl: './dashboard-home.component.html',
  styleUrl: './dashboard-home.component.css',
  viewProviders: [provideIcons({ lucidePackage, lucideTruck, lucideAlertTriangle, lucideBanknoteArrowUp })]
})
export class DashboardHomeComponent implements AfterViewInit, OnDestroy {
  totalProducts = 125;
  totalSuppliers = 18;
  lowStockProducts = 7;
  inventoryValue = 24500;

  @ViewChild("stockChart")
  private stockChartCanvas!: ElementRef<HTMLCanvasElement>;

  @ViewChild("movementChart")
  private movementChartCanvas!: ElementRef<HTMLCanvasElement>;

  private stockChart?: Chart;
  private movementChart?: Chart;

  constructor(private readonly dashboardService: DashboardService) { }

  ngAfterViewInit(): void {
    this.loadLowStockProducts();
    this.loadMovementSummary();
  }

  ngOnDestroy(): void {
    this.stockChart?.destroy();
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
            backgroundColor: "#5170ff",
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
            borderColor: "#5170ff",
            backgroundColor: "#5170ff",
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
}
