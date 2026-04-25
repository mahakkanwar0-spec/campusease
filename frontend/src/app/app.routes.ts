import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { ComplaintComponent } from './components/complaint/complaint';
import { UserProfileComponent } from './components/user-profile/user-profile';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'complaints', component: ComplaintComponent }, // 🔥 REQUIRED
  { path: 'profile', component: UserProfileComponent },
{
  path: 'admin',
  loadComponent: () =>
    import('./components/admin-dashboard/admin-dashboard')
      .then(m => m.AdminDashboardComponent),
  canActivate: [authGuard, adminGuard]
},

  { path: '**', redirectTo: 'login' }
];
