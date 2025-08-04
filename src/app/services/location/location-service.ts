import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {LocationSearch} from './location-search';
import {Router} from '@angular/router';
import {Pageable} from '../../data/interfaces/pageable';
import {UserLocation} from '../../data/interfaces/user-location';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LocationService {
  http = inject(HttpClient)
  router = inject(Router)
  baseUrl = environment.apiUrl

  searchLocation(params: { locationName: any }) {
    return this.http
      .get<LocationSearch[]>(this.baseUrl + '/locations/search', {params})
  }

  saveLocation(location: LocationSearch) {
    return this.http
      .post(this.baseUrl + '/location/save', location, {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        })
      })
  }

  getLocations(params: { pageNumber: any } = {pageNumber: 0}) {
    return this.http
      .get<Pageable<UserLocation>>(this.baseUrl + '/locations', {params})
  }

  deleteLocation(id: number) {
    return this.http
      .delete(this.baseUrl + '/location/' + id)
  }
}
