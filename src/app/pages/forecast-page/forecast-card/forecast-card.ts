import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {Forecast} from '../../../data/interfaces/forecast';
import {ImagePipe} from '../../../pipes/image-pipe';
import {DialogService} from '../../../shared/dialog/dialog-service';

@Component({
  selector: 'app-forecast-card',
  imports: [
    ImagePipe
  ],
  templateUrl: './forecast-card.html',
  styleUrl: './forecast-card.css'
})
export class ForecastCard {
  @Input() forecast!: Forecast
  @Output() deleteRequested = new EventEmitter<number>()

  dialog = inject(DialogService)

  deleteLocation() {
    this.dialog.openConfirmDialog({
      title: 'Deleting Location',
      message: 'Are you sure you want to delete this location?',
      cancelText: 'Cancel',
      confirmText: 'Delete'
    })
      .subscribe(ok => {
        if (ok) this.deleteRequested.emit(this.forecast.locationId)
      })
  }
}
