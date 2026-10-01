import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLink } from '@angular/router';
import { RegisterRequest } from '../../models/register-request';

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

  constructor(private readonly authService: AuthService, private readonly router: Router){

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
        this.router.navigate(["/dashboard"])
      },
      error: error => {
        console.log(error);
        this.errorMessage = "No se pudo completar el registro"
      }
    });
  }
}
