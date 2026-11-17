import { Component, inject, ChangeDetectionStrategy } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-change-password',
  imports: [ReactiveFormsModule],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.css',
  changeDetection: ChangeDetectionStrategy.Eager
})
export class ChangePasswordComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  saving = false;
  errorMessage = '';

  readonly form = this.formBuilder.nonNullable.group({
    currentPassword: ['', Validators.required],
    newPassword: ['', [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(64),
      Validators.pattern(
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).+$/
      )
    ]],
    confirmPassword: ['', Validators.required]
  }, {
    validators: control =>
      control.get('newPassword')?.value ===
      control.get('confirmPassword')?.value
        ? null
        : { passwordMismatch: true }
  });

  submit(): void {
    if (this.saving) {
      return;
    }

    this.form.markAllAsTouched();

    if (this.form.invalid) {
      return;
    }

    this.saving = true;
    this.errorMessage = '';

    this.authService.changePassword(this.form.getRawValue()).subscribe({
      next: () => {
        this.form.reset();
        this.authService.logout();

        void this.router.navigate(['/login'], {
          queryParams: { passwordChanged: 'true' },
          replaceUrl: true
        });
      },
      error: error => {
        this.saving = false;
        this.errorMessage =
          typeof error.error?.error === 'string'
            ? error.error.error
            : 'No se pudo cambiar la contraseña';
      }
    });
  }

  logout(): void {
    this.form.reset();
    this.authService.logout();
    void this.router.navigate(['/login'], { replaceUrl: true });
  }
}
