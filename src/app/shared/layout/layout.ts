import {Component, inject} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {Navigatebar} from '../navigatebar/navigatebar';
import {AuthService} from '../../services/auth/auth-service';
import {Searchbar} from '../searchbar/searchbar';
import {Footbar} from '../footbar/footbar';

@Component({
  selector: 'app-layout',
  imports: [
    RouterOutlet,
    Navigatebar,
    Searchbar,
    Footbar
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class Layout {
  authService = inject(AuthService);
}
