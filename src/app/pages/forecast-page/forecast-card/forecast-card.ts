import {Component, EventEmitter, inject, Input, Output} from '@angular/core';
import {Forecast} from '../../../data/interfaces/forecast';
import {LocationService} from '../../../services/location/location-service';
import {Router} from '@angular/router';
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
  @Output() deleted = new EventEmitter<number>()

  locationService = inject(LocationService)
  router = inject(Router)
  // dialog = inject(MatDialog)
  dialog = inject(DialogService)

  deleteLocation() {
    this.dialog.openConfirmDialog({
      title: 'Deleting Location',
      message: 'Are you sure you want to delete this location?',
      cancelText: 'Cancel',
      confirmText: 'Delete'
    })
      .subscribe(result => {
        if (result) {
          this.locationService.deleteLocation(this.forecast.locationId)
            .subscribe({
              next: () => {
                this.deleted.emit(this.forecast.locationId)
              }
            })
        }
      })


    // if (!confirm('Are you sure you want to delete this location?')) {
    //   return
    // }

    // const dialogRef = this.dialog.open(ConfirmDialog)
    //
    // dialogRef.afterClosed().subscribe(result => {
    //   if (result) {
    //     this.locationService.deleteLocation(this.forecast.locationId)
    //       .subscribe({
    //         next: () => {
    //           this.deleted.emit(this.forecast.locationId)
    //         }
    //       })
    //   }
    // })

    // this.locationService.deleteLocation(this.forecast.locationId)
    //   .subscribe(({
    //     next: () => {
    //       this.deleted.emit(this.forecast.locationId)
    //     }
    //   }))
  }
}
