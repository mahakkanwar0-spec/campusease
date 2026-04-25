import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API } from '../helpers/api.helper';

@Injectable({ providedIn: 'root' })
export class AuthService {

  constructor(private http: HttpClient) {}

  register(data: any) {
    return this.http.post(API.BASE + API.AUTH.REGISTER, data);
  }

  login(data: any) {
    return this.http.post(API.BASE + API.AUTH.LOGIN, data);
  }

  updateProfile(data: any) {
    return this.http.put(API.BASE + API.AUTH.UPDATE, data);
  }

  deleteAccount() {
    return this.http.delete(API.BASE + API.AUTH.DELETE);
  }

  getCurrentUser() {
    try {
      const user = localStorage.getItem('user');
      return user ? JSON.parse(user) : null;
    } catch {
      localStorage.removeItem('user');
      return null;
    }
  }

  getUserRole() {
    const user = this.getCurrentUser();
    return user?.role || null;
  }


  logout() {
  localStorage.removeItem('user');
  localStorage.removeItem('token');
}

isLoggedIn(): boolean {
  return !!localStorage.getItem('user');
}

}


