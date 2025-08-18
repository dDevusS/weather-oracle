import {HttpHandlerFn, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from '../../services/auth/auth-service';
import {BehaviorSubject, catchError, filter, finalize, switchMap, take, throwError} from 'rxjs';

const isRefreshing$ = new BehaviorSubject<boolean>(false);

export const authTokenInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.url.includes('/api/auth/')) {
    return next(req)
  }

  const authService = inject(AuthService);
  const accessToken = authService.accessToken

  if (!accessToken) return refreshAndProceed(authService, req, next)

  if (isRefreshing$.value) {
    return refreshAndProceed(authService, req, next)
  }

  return next(addToken(req, accessToken))
    .pipe(
      catchError(error => {
        if (error.status === 401) {
          return refreshAndProceed(authService, req, next)
        }

        return throwError(() => error)
      })
    )
};

const refreshAndProceed = (authService: AuthService, req: HttpRequest<any>, next: HttpHandlerFn) => {
  if (!isRefreshing$.value) {
    isRefreshing$.next(true)

    return authService.refreshToken().pipe(
      switchMap(() => {
        return next(addToken(req, authService.accessToken!))
      }),
      catchError(error => {
        if (error.status === 401) {
          authService.logout()
        }
        return throwError(() => error)
      }),
      finalize(() => {
        isRefreshing$.next(false)
      })
    )
  }

  if (req.url.includes('refresh')) {
    return next(addToken(req, authService.accessToken!))
  }

  return isRefreshing$.pipe(
    filter(isRefreshing => !isRefreshing),
    take(1),
    switchMap(() => {
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
