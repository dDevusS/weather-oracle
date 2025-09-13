import {Component, inject} from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from "@angular/forms";
import {AuthService} from '../../../services/auth/auth-service';
import {Router} from '@angular/router';
import {Location} from '@angular/common';
import {LoadingState} from '../../../shared/loading-state';
import {finalize} from 'rxjs';

@Component({
  selector: 'app-registration-page',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './registration-page.html',
  styleUrls: [
    './registration-page.css',
    '../auth-page.css'
  ]
})
export class RegistrationPage {
  authService = inject(AuthService)
  router = inject(Router)
  location = inject(Location)
  minLengthForLogin = 3
  maxLengthForLogin = 20
  minLengthForPassword = 6
  nonLettersRequirement = 1

  isLoading = inject(LoadingState).isLoading
  errorMessages: string[] = [];
  form: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({
        id: new FormControl(0),
        login: new FormControl(null,
          [
            Validators.required,
            Validators.minLength(this.minLengthForLogin),
            Validators.maxLength(this.maxLengthForLogin),
            this.noSpacesValidator(),
            this.allowedSymbolsValidator()
          ]
        ),
        rawPassword: new FormControl(null,
          [
            Validators.required,
            Validators.minLength(this.minLengthForPassword),
            this.noSpacesValidator(),
            this.allowedSymbolsValidator(),
            this.nonLetterSymbolsRequiredValidator(this.nonLettersRequirement)
          ]
        ),
        confirmRawPassword: new FormControl(null, [Validators.required])
      },
      {validators: this.passwordMatchValidator}
    )
  }

  getErrors(control: AbstractControl | null, field: string = ''): string[] {
    if (!control || (!control.touched && !control.dirty)) return [];

    const errors = control.errors;
    if (!errors) return [];

    const messages: string[] = [];

    if (errors['required']) messages.push('All fields are required.');
    if (errors['minlength']) messages.push(`${field} must be contain from ${errors['minlength'].requiredLength} symbols.`);
    if (errors['mismatch']) messages.push(`Passwords should be match.`);
    if (errors['noSpaces']) messages.push(`${field} must be without spaces.`);
    if (errors['invalidSymbols']) messages.push(`${field} must be written with only latin letters.`);
    if (errors['nonLettersRequired']) messages.push(`${field} must contain at least ${this.nonLettersRequirement} non letter symbols.`);

    return messages;
  }

  passwordMatchValidator: ValidatorFn = (control: AbstractControl) => {
    const form = control as FormGroup;
    const rawPassword = form.get('rawPassword')?.value;
    const confirmPassword = form.get('confirmRawPassword')?.value;
    return rawPassword === confirmPassword ? null : {mismatch: true}
  }

  nonLetterSymbolsRequiredValidator(minCount: number) {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      if (!value) return null;

      const nonLetters = value.replace(/[a-zA-Z]/g, '')
      return nonLetters.length >= minCount ? null : {nonLettersRequired: true}
    }
  }

  noSpacesValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      return /\s/.test(control.value) ? {noSpaces: true} : null;
    };
  }

  allowedSymbolsValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      return /^[a-zA-Z0-9!@#$%^&*()\-=+[\]{}|;:'",.<>?/\s]+$/.test(control.value)
        ? null
        : {invalidSymbols: true};
    };
  }

  goBack(): void {
    this.location.back()
  }

  onSubmit() {
    this.errorMessages = [];
    this.form.markAllAsTouched();

    this.errorMessages = [
      ...this.getErrors(this.form.get('login'), 'Login'),
      ...this.getErrors(this.form.get('rawPassword'), 'Password'),
      ...this.getErrors(this.form.get('confirmRawPassword')),
      ...this.getErrors(this.form)
    ]

    if (this.form.valid) {
      this.isLoading.set(true)

      this.authService.register(this.form.value)
        .pipe(
          finalize(() => {
            this.isLoading.set(false)
          })
        )
        .subscribe({
          next: () => {
            this.router.navigateByUrl('login').then(

            )
          },
          error: error => {
            if (error.status === 409) {
              this.errorMessages.push('User with this login already exists.')
            } else {
              this.errorMessages.push('Something went wrong. Please try again later.')
            }
          }
        })
    }
    else {
      console.log("Invalid form");
    }
  }
}
