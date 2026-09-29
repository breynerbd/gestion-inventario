import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../models/auth/register-request';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  username = "";
  password = "";
  firstNames = "";
  lastNames = "";
  email = "";
  phone = "";
  errorMessage = "";

  constructor(private authService: AuthService){

  }

  register(): void{
    const request: RegisterRequest = {
      username: this.username,
      password: this.password,
      firstNames: this.firstNames,
      lastNames: this.lastNames,
      email: this.email,
      phone: this.phone
    };

    this.authService.register(request).subscribe({
      next: response => {
        this.authService.saveSession(response);
        this.errorMessage = "";
      },
      error: error => {
        console.log(error);
        this.errorMessage = "No se pudo completar el registro"
      }
    });
  }
}
