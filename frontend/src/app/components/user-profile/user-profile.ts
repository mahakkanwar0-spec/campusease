import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-profile.html',
  styleUrls: ['./user-profile.css']
})
export class UserProfileComponent implements OnInit {

  user: any = {};
  email = '';
  password = '';

  private api = 'http://localhost:8080/api/auth';
;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    const stored = localStorage.getItem('user');
    if (stored) {
      this.user = JSON.parse(stored);
      this.email = this.user.email;
    }
  }

 updateProfile() {
  this.http.put(`${this.api}/update`, {
    email: this.email,
    password: this.password
  }).subscribe(() => alert('✅ Profile updated'));
}


  deleteAccount() {
    if (!confirm('Delete account permanently?')) return;

    this.http.delete(`${this.api}/delete`).subscribe({
      next: () => this.afterDelete(),
      error: () => this.afterDelete() // backend deletes but returns empty
    });
  }

  private afterDelete() {
    localStorage.clear();
    this.router.navigate(['/register']);
  }
}
