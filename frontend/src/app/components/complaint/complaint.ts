import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { API } from '../../helpers/api.helper';
import { Router } from '@angular/router';
@Component({
  selector: 'app-complaints',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './complaint.html',
  styleUrls: ['./complaint.css']
})

export class ComplaintComponent implements OnInit {

  complaints: any[] = [];
  form = { title: '', description: '' };

  private api = 'http://localhost:8080/api/complaints';

  constructor(private http: HttpClient,private router: Router) {}

ngOnInit() {
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  if (user.role === 'ROLE_ADMIN') {
    this.loadAllComplaints();
  } else {
    this.loadComplaints();
  }
}


loadAllComplaints() {
  this.http.get<any[]>(
    'http://localhost:8080/api/complaints/all'
  ).subscribe({
    next: res => this.complaints = res,
    error: err => console.error(err)
  });
}

  loadComplaints() {
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  if (!user.id) return;

  this.http.get<any[]>(
    `http://localhost:8080/api/complaints/user/${user.id}`
  ).subscribe({
    next: res => this.complaints = res,
    error: err => console.error(err)
  });
}


  create() {
    this.http.post('http://localhost:8080/api/complaints/create', this.form).subscribe(() => {
      this.form = { title: '', description: '' };
      this.loadComplaints();
    });
  }
}
