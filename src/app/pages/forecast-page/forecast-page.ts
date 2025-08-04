import {Component, DestroyRef, inject, OnInit, signal} from '@angular/core';
import {LocationService} from '../../services/location/location-service';
import {UserLocation} from '../../data/interfaces/user-location';
import {Forecast} from '../../data/interfaces/forecast';
import {ForecastService} from '../../services/forecast-service';
import {catchError, finalize, of, switchMap, tap} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {takeUntilDestroyed} from '@angular/core/rxjs-interop';
import {ForecastCard} from './forecast-card/forecast-card';

@Component({
  selector: 'app-forecast-page',
  imports: [
    ForecastCard
  ],
  templateUrl: './forecast-page.html',
  styleUrl: './forecast-page.css'
})
export class ForecastPage implements OnInit {
  locationService = inject(LocationService)
  forecastService = inject(ForecastService)
  router = inject(Router)
  route = inject(ActivatedRoute)
  destroyRef = inject(DestroyRef)

  first: boolean = true
  last: boolean = true
  currentPage: number = 0
  forecasts: Forecast[] = [];

  isLoading = signal(false)
  error = signal<string | null>(null)

  ngOnInit() {
    this.route.queryParams.pipe(
      takeUntilDestroyed(this.destroyRef)
    ).subscribe(params => {
      this.currentPage = Number(params['pageNumber']) || 0;
      this.loadData({pageNumber: this.currentPage})
    });
  }

  handleDelete(locationId: number) {
    this.isLoading.set(true);

    if (this.forecasts.length === 1 && this.currentPage > 0) {
      const newPage = this.currentPage - 1;
      this.router.navigate([], {
        queryParams: { pageNumber: newPage },
        queryParamsHandling: 'merge'
      }).then(() => {
        this.loadData({ pageNumber: newPage });
      });
    } else {
      this.loadData({ pageNumber: this.currentPage });
    }
  }

  nextPage() {
    this.updatePage(this.currentPage + 1)
  }

  previousPage() {
    this.updatePage(this.currentPage - 1)
  }

  private updatePage(newPage: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { pageNumber: newPage > 0 ? newPage : 0 }
    });
  }

  private loadData(params: { pageNumber: number } = {pageNumber: 0}) {
    this.isLoading.set(true)
    this.error.set(null)

    this.loadLocations(params)
      .pipe(
        switchMap(locations => {
          this.first = locations.first
          this.last = locations.last

          return this.loadForecasts(locations.content)
        }),
        catchError(error => {
            return of(null)
          }
        ),
        finalize(() => {
          this.isLoading.set(false)
        }),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe()
  }

  private loadForecasts(locations: UserLocation[]) {
    if (!locations.length) {
      this.forecasts = [];
      return of(null);
    }

    return this.forecastService.getForecasts(locations)
      .pipe(
        tap(forecasts => {
          this.forecasts = forecasts;
        })
      )
  }

  private loadLocations(params: { pageNumber: number } = {pageNumber: 0}) {
    return this.locationService.getLocations(params)
  }

}
