import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from '../../../auth/services/auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink, RouterOutlet],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  username = "";
  roleName = "";

  constructor(private readonly authService: AuthService, private readonly router: Router){
    this.username = this.authService.getUsername() || "";
    this.roleName = this.authService.getRoleName() || "";
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(["/login"]);
  }

}
