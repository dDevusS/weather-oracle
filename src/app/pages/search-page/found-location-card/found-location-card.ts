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
export class FoundLocationCard {
  @Input() location!: LocationSearch
  @Input() isDuplicated = false
  @Output() saveRequested = new EventEmitter<LocationSearch>()

  router = inject(Router);

  saveLocation() {
    this.saveRequested.emit(this.location)
  }

  getErrorMessage() {
    if (this.isDuplicated) {
      return ERROR_DUPLICATE_LOCATION;
    }

    return null
  }
}
