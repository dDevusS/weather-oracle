import {Component, EventEmitter, inject, Input, OnChanges, Output, signal, SimpleChanges} from '@angular/core';
import {LocationSearch} from '../../../services/location/location-search';
import {CoordinatePipe} from '../../../pipes/coordinate-pipe';
import {LocationService} from '../../../services/location/location-service';
import {Router} from '@angular/router';

const ERROR_DUPLICATE_LOCATION = "This location already exists"

@Component({
  selector: 'app-found-location-card',
  imports: [
    CoordinatePipe
  ],
  templateUrl: './found-location-card.html',
  styleUrl: './found-location-card.css'
})
export class FoundLocationCard implements OnChanges {
  @Input() location!: LocationSearch
  @Input() clearError!: boolean

  locationService = inject(LocationService)
  router = inject(Router);
  errorMessage: string | null = null

  ngOnChanges() {
    if (this.clearError) {
      this.errorMessage = null
    }
  }

  saveLocation() {
    this.locationService.saveLocation(this.location)
      .subscribe({
        next: () => {
          this.router.navigate(['']);
        },
        error: error => {
          if (error.status === 409) {
            this.errorMessage = ERROR_DUPLICATE_LOCATION
          }
        }
      })
  }
}
