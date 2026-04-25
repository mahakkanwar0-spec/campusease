import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './navbar.html',
  styleUrls: ['./navbar.css']
})
export class NavbarComponent {

  constructor(private router: Router) {}

  isLoggedIn() {
    return !!localStorage.getItem('token');
  }

 isAdmin() {
  const user = JSON.parse(localStorage.getItem('user') || '{}');
  return user.role === 'ROLE_ADMIN';
}


  isActive(path: string) {
    return this.router.url === path;
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
