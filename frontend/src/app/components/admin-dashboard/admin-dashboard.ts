import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ComplaintService } from '../../services/complaint/complaint';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css'
})
export class AdminDashboardComponent implements OnInit {

  complaints: any[] = [];

  constructor(private complaintService: ComplaintService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.complaintService.getAll().subscribe({
      next: (res) => {
        this.complaints = res as any[];
      },
      error: (err) => {
        console.error('Error loading complaints', err);
      }
    });
  }

  updateStatus(c: any, status: string) {
    this.complaintService.updateStatus(c.id, status).subscribe({
      next: () => {
        c.status = status;   
      },
      error: (err) => {
        console.error('Error updating status', err);
      }
    });
  }
}
