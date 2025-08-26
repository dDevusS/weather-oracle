import {Component, inject} from '@angular/core';
import {Svg} from '../svg/svg';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../services/auth/auth-service';
import {DialogService} from '../dialog/dialog-service';
import {LoadingState} from '../loading-state';
import {finalize} from 'rxjs';

@Component({
  selector: 'app-navigatebar',
  imports: [
    Svg,
    RouterLink
  ],
  templateUrl: './navigatebar.html',
  styleUrl: './navigatebar.css'
})
export class Navigatebar {
  router = inject(Router)
  authService = inject(AuthService)
  dialog = inject(DialogService)

  get isAuthPage(): boolean {
    return this.router.url === '/login' || this.router.url === '/registration'
  }

  logout() {
    this.dialog.openConfirmDialog({
      title: 'Logout',
      message: 'Are you sure you want to logout?',
    })
      .subscribe((ok) => {
        if (ok) {
          this.authService.logout()
        }
      })
  }

}
