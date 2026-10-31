import { Component, ChangeDetectionStrategy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { LoginRequest } from '../../models/login-request';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  changeDetection: ChangeDetectionStrategy.Eager,
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username = "";
  password = "";
  errorMessage = "";

  constructor(private readonly authService: AuthService, private readonly router: Router){

  }

  login(): void {
    const request: LoginRequest = {
      username: this.username,
      password: this.password
    }

    this.authService.login(request).subscribe({
      next: response => {
        this.authService.saveSession(response);
        this.errorMessage = '';
        void this.router.navigate(["/dashboard"]);
      },
      error: error => {
        console.error(error);

        const message = error.error?.error?.toLowerCase();

        if (message?.includes('bloqueado')) {
          this.errorMessage = 'Tu cuenta está bloqueada.';
        } else if (message?.includes('inactivo')) {
          this.errorMessage = 'Tu cuenta está inactiva.';
        } else {
          this.errorMessage = 'Usuario o contraseña incorrectos.';
        }
      }
    });
  }
}
