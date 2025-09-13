import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';
import {finalize, tap} from 'rxjs';
import {TokenResponse} from './token-response';
import {environment} from '../../../environments/environment';
import {LoadingState} from '../../shared/loading-state';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient)
  private cookieService = inject(CookieService)
  private router = inject(Router)
  private readonly baseApiUrl = environment.apiUrl
  isLoading = inject(LoadingState).isLoading

  get isAuthenticated(): boolean {
    return !!this.accessToken;
  }

  get accessToken(): string | null {
    return this.cookieService.get('accessToken') || null;
  }

  login(payload: { login: string, password: string }) {
    return this.http.post<TokenResponse>(
      this.baseApiUrl + '/auth/login',
      payload, {
        withCredentials: true,
        headers: {'Content-Type': 'application/json'}
      })
      .pipe(
        tap(value => this.saveAccessToken(value))
      )
  }

  refreshToken() {
    return this.http.post<TokenResponse>(
      this.baseApiUrl + '/auth/refresh',
      {},
      {
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
    this.isLoading.set(true)

    this.http.post(this.baseApiUrl + '/auth/logout',
      {},
      {
        withCredentials: true
      })
      .pipe(
        finalize(() => {
          this.isLoading.set(false)
        })
      )
      .subscribe({
        complete: () => {
          this.router.navigate(['login'])
        }
      })
  }

  register(payload: { login: string, rawPassword: string, confirmRawPassword: string }) {
    return this.http.post(this.baseApiUrl + "/auth/registration",
      payload,
      {
        headers: {'Content-Type': 'application/json'}
      })
  }

  private saveAccessToken(tokenResponse: TokenResponse) {
    if (tokenResponse?.accessToken) {
      this.cookieService.set('accessToken', tokenResponse.accessToken)
    }
  }
}
