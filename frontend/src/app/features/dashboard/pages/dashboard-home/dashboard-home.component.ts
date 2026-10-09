import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnDestroy, ViewChild } from "@angular/core";
import { DashboardService } from '../../services/dashboard.service';
import { LowStockProduct } from '../../models/low-stock-product';
import { Chart, ChartConfiguration, registerables } from "chart.js";
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { lucidePackage, lucideTruck, lucideAlertTriangle, lucideBanknoteArrowUp } from '@ng-icons/lucide';

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

  private stockChart?: Chart;

  constructor(private readonly dashboardService: DashboardService) { }

  ngAfterViewInit(): void {
    this.loadLowStockProducts();
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
}
