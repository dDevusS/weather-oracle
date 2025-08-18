import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';
import {tap} from 'rxjs';
import {TokenResponse} from './token-response';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  http = inject(HttpClient)
  cookieService = inject(CookieService)
  router = inject(Router)
  baseApiUrl = environment.apiUrl

  get isAuthenticated(): boolean {
      return !!this.accessToken;
  }

  get accessToken(): string {
    return this.cookieService.get('accessToken');
  }

  login(payload: {login: string, password: string}) {
    return this.http.post<TokenResponse>(this.baseApiUrl + '/auth/login', payload, {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    })
      .pipe(
        tap(value => this.saveAccessToken(value))
      )
  }

  refreshToken() {
    return this.http.post<TokenResponse>(this.baseApiUrl + '/auth/refresh', {}, {
      withCredentials: true
    })
      .pipe(
        tap(value => {
          this.saveAccessToken(value)
        })
      )
  }

  logout() {
    this.cookieService.deleteAll()

          this.http.post(this.baseApiUrl + '/auth/logout', {}, {
            withCredentials: true
          })
            .subscribe({
              next: () => {
                this.router.navigate(['login']);
              }
            })
        }
      })
  }

  register(payload: {login: string, rawPassword: string, confirmRawPassword: string}) {
    return this.http.post(this.baseApiUrl + "/auth/registration", payload, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    })
  }

  saveAccessToken(tokenResponse: TokenResponse) {
    this.cookieService.set('accessToken', tokenResponse.access_token)
  }
}
