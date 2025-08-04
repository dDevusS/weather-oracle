import {CanActivateChildFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../../services/auth/auth-service';

export const accessGuard: CanActivateChildFn = (childRoute, state) => {
  const isAuthenticated = inject(AuthService).isAuthenticated

  if (isAuthenticated) {
    return true
  }

  return inject(Router).createUrlTree(['/login'])
};
