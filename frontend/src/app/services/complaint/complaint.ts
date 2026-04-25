import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API } from '../../helpers/api.helper';

@Injectable({ providedIn: 'root' })
export class ComplaintService {
  constructor(private http: HttpClient) {}

  create(data: any) {
    return this.http.post(API.BASE + API.COMPLAINTS.CREATE, data);
  }

  getByUser(userId: string) {
    return this.http.get(API.BASE + API.COMPLAINTS.BY_USER(userId));
  }
  getComplaints() {
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  if (user.role === 'ROLE_ADMIN') {
    return this.http.get(`${API.BASE}/api/complaints/all`);
  } else {
    return this.http.get(`${API.BASE}/api/complaints/my`);
  }
}


  getById(id: string) {
    return this.http.get(API.BASE + API.COMPLAINTS.BY_ID(id));
  }

  update(id: string, data: any) {
    return this.http.put(API.BASE + API.COMPLAINTS.UPDATE(id), data);
  }

  delete(id: string) {
    return this.http.delete(API.BASE + API.COMPLAINTS.DELETE(id));
  }

  getAll() {
    return this.http.get(API.BASE + API.COMPLAINTS.ALL);
  }

updateStatus(id: string, status: string) {
  return this.http.put(
    `${API.BASE}/api/complaints/${id}/status`,
    { status: status }
  );
}

}
