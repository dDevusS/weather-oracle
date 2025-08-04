import {Component, inject} from '@angular/core';
import {Svg} from '../svg/svg';
import {NgIf} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../services/auth/auth-service';

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
  authService = inject(AuthService);

  get isAuthPage(): boolean {
    return this.router.url === '/login' || this.router.url === '/registration'
  }

}
