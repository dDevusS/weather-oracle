import {Injectable, signal} from '@angular/core';
import {LocationSearch} from './location-search';

@Injectable({
  providedIn: 'root'
})
export class LocationSearchState {
  locations = signal<LocationSearch[]>([])
  error = signal<string | null>(null)

  setLocations(locations: LocationSearch[]) {
    this.locations.set(locations)
  }

  setError(message: string) {
    this.error.set(message)
  }

  eraseError() {
    this.error.set(null)
  }
}
