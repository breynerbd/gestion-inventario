import { Component, EventEmitter, Input, Output, ChangeDetectionStrategy } from '@angular/core';
import { DetailField } from '../../models/detail-field';

@Component({
  selector: 'app-detail-modal',
  imports: [],
  templateUrl: './detail-modal.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './detail-modal.component.css'
})
export class DetailModalComponent {
  @Input() title = 'Detalles del registro';
  @Input() fields: DetailField[] = [];
  @Input() visible = false;

  @Output() closed = new EventEmitter<void>();

  close(): void {
    this.closed.emit();
  }

  onDialogClick(event: MouseEvent): void {
    if (event.target === event.currentTarget) {
      this.close();
    }
  }
}
