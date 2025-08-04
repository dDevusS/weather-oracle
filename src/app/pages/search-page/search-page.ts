import {Component, inject, signal} from '@angular/core';
import {LocationSearchState} from '../../services/location/location-search-state';
import {LocationService} from '../../services/location/location-service';
import {Router} from '@angular/router';
import {FoundLocationCard} from './found-location-card/found-location-card';

@Component({
  selector: 'app-search-page',
  imports: [
    FoundLocationCard
  ],
  templateUrl: './search-page.html',
  styleUrl: './search-page.css'
})
export class SearchPage {
  locationSearchState = inject(LocationSearchState)
  router = inject(Router)

  locations = this.locationSearchState.locations
  errorMessage = this.locationSearchState.error
  duplicateErrorMessage = signal<string | null>(null)

  clearErrorMessage() {
    this.errorMessage.set(null)
  }
}
