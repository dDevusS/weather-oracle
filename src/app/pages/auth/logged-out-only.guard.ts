import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../../services/auth/auth-service';

export const loggedOutOnlyGuard: CanActivateFn = (route, state) => {
  const isAuthenticated = inject(AuthService).isAuthenticated

  if (!isAuthenticated) {
    return true;
  }

  return inject(Router).createUrlTree([''])
};
