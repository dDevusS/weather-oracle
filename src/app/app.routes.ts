import { Routes } from '@angular/router';
import {Layout} from './shared/layout/layout';
import {LoginPage} from './pages/auth/login-page/login-page';
import {RegistrationPage} from './pages/auth/registration-page/registration-page';
import {SearchPage} from './pages/search-page/search-page';
import {accessGuard} from './pages/auth/access.guard';
import {ForecastPage} from './pages/forecast-page/forecast-page';

export const routes: Routes = [
  {
    path: '',
    component: Layout,
    children: [
      {
        path: '',
        component: ForecastPage
      },
      {
        path: 'login',
        component: LoginPage
      },
      {
        path: 'registration',
        component: RegistrationPage
      },
      {
        path: 'search',
        component: SearchPage,
        canActivate: [accessGuard]
      }
    ]
  }
];
