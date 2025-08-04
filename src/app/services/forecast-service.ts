import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {UserLocation} from '../data/interfaces/user-location';
import {Forecast} from '../data/interfaces/forecast';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ForecastService {
  http = inject(HttpClient)
  router = inject(Router)
  baseUrl = environment.apiUrl;

  getForecasts(locations: UserLocation[]) {
    return this.http
      .post<Forecast[]>(this.baseUrl + '/forecast', locations, {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      })
  }
}
