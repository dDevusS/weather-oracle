import {Component, inject, signal} from '@angular/core';
import {LocationSearchState} from '../../services/location/location-search-state';
import {LocationService} from '../../services/location/location-service';
import {Router} from '@angular/router';
import {FoundLocationCard} from './found-location-card/found-location-card';
import {LocationSearch} from '../../services/location/location-search';
import {LoadingState} from '../../shared/loading-state';
import {finalize} from 'rxjs';

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
  locationService = inject(LocationService)
  router = inject(Router)

  locations = this.locationSearchState.locations
  errorMessage = this.locationSearchState.error
  isLoading = inject(LoadingState).isLoading
  private duplicates = signal<Set<string>>(new Set())

  keyOf(location: LocationSearch): string {
    const lat = Number(location.lat).toFixed(4);
    const lon = Number(location.lon).toFixed(4);
    return `${location.name}|${location.country}|${location.state ?? ''}|${lat}|${lon}`;
  }

  isDuplicated(location: LocationSearch): boolean {
    return this.duplicates().has(this.keyOf(location));
  }

  addDuplicate(location: LocationSearch) {
    this.duplicates.update(set => {
      set.add(this.keyOf(location))
      return new Set(set)
    })
  }

  clearAllDuplicates() {
    this.duplicates.set(new Set());
  }

  onSaveRequested(location: LocationSearch) {
    this.isLoading.set(true)

    this.locationService.saveLocation(location)
      .pipe(
        finalize(() => {
          this.isLoading.set(false)
        })
      )
      .subscribe({
        next: () => {
          this.clearAllDuplicates()
          this.router.navigate([''])
        },
        error: error => {
          if (error.status === 409) {
            this.addDuplicate(location)
          }
        }
      })
  }

}
