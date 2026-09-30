import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  imports: [RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  username = "";
  roleName = "";

  constructor(private authService: AuthService, private router: Router){
    this.username = this.authService.getUsername() || "";
    this.roleName = this.authService.getRoleName() || "";
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(["/login"]);
  }

}
