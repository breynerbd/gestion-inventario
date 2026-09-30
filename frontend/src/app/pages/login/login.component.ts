import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { LoginRequest } from '../../models/auth/login-request';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username = "";
  password = "";
  errorMessage = "";

  constructor(private authService: AuthService, private router: Router){

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
        this.router.navigate(["/dashboard"]);
      },
      error: error => {
        console.error(error);
        this.errorMessage = "Credenciales incorrectas"
      }
    });
  }  
}
