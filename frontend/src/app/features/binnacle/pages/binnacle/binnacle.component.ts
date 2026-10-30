
import { Component, OnInit, ChangeDetectionStrategy } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { DatePipe } from "@angular/common";

import { BinnacleResponse } from "../../models/binnacle-response";
import { BinnacleService } from "../../services/binnacle.service";
import { PaginationComponent } from "../../../../shared/components/pagination/pagination.component";

@Component({
  selector: "app-binnacle",
  imports: [FormsModule, PaginationComponent, DatePipe],
  templateUrl: "./binnacle.component.html",
  styleUrl: "./binnacle.component.css",
  changeDetection: ChangeDetectionStrategy.Eager
})
export class BinnacleComponent implements OnInit {

  logs: BinnacleResponse[] = [];

  errorMessage = "";
  searchTerm = "";
  selectedResult = "";

  currentPage = 1;
  pageSize = 12;

  constructor(private readonly binnacleService: BinnacleService) { }

  ngOnInit(): void {
    this.loadLogs();
  }

  loadLogs(): void {
    this.binnacleService.getLogs().subscribe({
      next: response => {
        this.logs = response;
        this.errorMessage = "";
      },
      error: error => {
        console.error(error);
        this.errorMessage = "No se pudieron cargar los registros de bitácora";
      }
    });
  }

  get filteredLogs(): BinnacleResponse[] {
    const search = this.searchTerm.trim().toLowerCase();

    return this.logs.filter(log =>
      log.logId.toString().includes(search) ||
      log.username.toLowerCase().includes(search) ||
      log.result.toLowerCase().includes(search)
    ).filter(log =>
      this.selectedResult === "" ||
      log.result === this.selectedResult
    );
  }

  get totalPages(): number {
    return Math.ceil(this.filteredLogs.length / this.pageSize);
  }

  get paginatedLogs(): BinnacleResponse[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;

    return this.filteredLogs.slice(
      startIndex,
      startIndex + this.pageSize
    );
  }

  onSearchChange(value: string): void {
    this.searchTerm = value;
    this.currentPage = 1;
  }

  onResultChange(value: string): void {
    this.selectedResult = value;
    this.currentPage = 1;
  }

  changePage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
    }
  }
}
