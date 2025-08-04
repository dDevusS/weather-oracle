import {HttpHandlerFn, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from '../../services/auth/auth-service';
import {BehaviorSubject, catchError, filter, switchMap, tap, throwError} from 'rxjs';

let isRefreshing$ = new BehaviorSubject<boolean>(false);

export const authTokenInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.url.includes('/api/auth/')) {
    return next(req)
  }

  const authService = inject(AuthService);
  const accessToken = authService.accessToken

  if (!accessToken) return next(req)

  if (isRefreshing$.value) {
    return refreshAndProceed(authService, req, next)
  }

  return next(addToken(req, accessToken))
    .pipe(
      catchError(error => {
        if (error.status === 401) {
          return refreshAndProceed(authService, req, next)
        }

        return throwError(error)
      })
    )
};

const refreshAndProceed = (authService: AuthService, req: HttpRequest<any>, next: HttpHandlerFn) => {
  if (!isRefreshing$.value) {
    isRefreshing$.next(true);

    return authService.refreshToken()
      .pipe(
        switchMap(res => {

          return next(addToken(req, res.access_token))
            .pipe(
              tap(() => isRefreshing$.next(false))
            )
        })
      )
  }

  if (req.url.includes('refresh')) return next(addToken(req, authService.accessToken!))

  return isRefreshing$.pipe(
    filter(isRefreshing => !isRefreshing),
    switchMap(res => {
      return next(addToken(req, authService.accessToken!))
    })
  )
}

const addToken = (req: HttpRequest<unknown>, accessToken: string) => {
  return req.clone({
    setHeaders: {
      Authorization: `Bearer ${accessToken}`
    }
  })
}
