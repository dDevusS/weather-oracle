import {Component, inject} from '@angular/core';
import {AuthService} from '../../../services/auth/auth-service';
import {Router} from '@angular/router';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {Location} from '@angular/common';

@Component({
  selector: 'app-login-page',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './login-page.html',
  styleUrls: [
    './login-page.css',
    '../auth-page.css'
  ]
})
export class LoginPage {
  authService = inject(AuthService)
  router = inject(Router)
  location = inject(Location)

  errorMessage: string | null = null;

  form: FormGroup = new FormGroup({
    login: new FormControl(null, [Validators.required]),
    password: new FormControl(null, [Validators.required])
  })

  goBack(): void {
    this.location.back();
  }

  onSubmit() {
    this.errorMessage = null;

    if (this.form.valid) {
      this.authService.login(this.form.value)
        .subscribe({
          next: () => {
            this.router.navigate([''])
          },
          error: err => {
            if (err.status === 401) {
              this.errorMessage = 'Login or password is incorrect'
            }
            else {
              this.errorMessage = 'Something went wrong. Please try again later.'
            }
          }
        })
    }
  }
}
