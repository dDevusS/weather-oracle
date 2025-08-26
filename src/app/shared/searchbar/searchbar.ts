import {Component, DestroyRef, inject, OnInit} from '@angular/core';
import {LocationService} from '../../services/location/location-service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {LocationSearchState} from '../../services/location/location-search-state';
import {catchError, finalize, of, switchMap} from 'rxjs';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {LoadingState} from '../loading-state';

const ERROR_EMPTY_QUERY = "Location name should not be blank and must contain at least three characters.";
const ERROR_NOT_FOUND = "Locations were not found.";
const ERROR_UNKNOWN = "An unknown error occurred. Please try again later.";

@Component({
  selector: 'app-searchbar',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './searchbar.html',
  styleUrl: './searchbar.css'
})
export class Searchbar implements OnInit {
  locationService = inject(LocationService)
  locationSearchState = inject(LocationSearchState)
  router = inject(Router)
  route = inject(ActivatedRoute)
  isLoading = inject(LoadingState).isLoading

  private destroyRef = inject(DestroyRef)

  searchForm = new FormGroup({
    locationName: new FormControl('', [Validators.required, Validators.minLength(3)])
  })

  get isSearchPage(): boolean {
    return this.router.url === '/search'
  }

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      if (params['locationName']) {
        this.searchForm.patchValue(params['locationName']);
      }
    })
  }

  onSubmit() {
    this.navigateToSearchPageIfNeeded()

    if (!this.searchForm.valid) {
      this.locationSearchState.setError(ERROR_EMPTY_QUERY)
      this.navigateToSearchPageIfNeeded()
      return
    }

    this.isLoading.set(true)

    this.locationService.searchLocation({locationName: this.searchForm.value.locationName})
      .pipe(
        switchMap(locations => {
          this.locationSearchState.setLocations(locations)
          this.locationSearchState.eraseError()
          return of(null)
        }),
        catchError(error => {
          const errorMessage = error.status === 404 ? ERROR_NOT_FOUND : ERROR_UNKNOWN
          this.locationSearchState.setError(errorMessage)
          this.locationSearchState.setLocations([])
          return of(null)
        }),
        finalize(() => {
          this.isLoading.set(false)
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe()
  }

  navigateToSearchPageIfNeeded() {
    if (!this.isSearchPage) this.router.navigate(['search'], {
      queryParams: { locationName: this.searchForm.value.locationName }
    })
  }

}
