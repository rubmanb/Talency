import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { DashboardComponent } from './layout/dashboard/dashboard.component';


export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    children: [
      { path: 'employees', loadComponent: () => import('./pages/employee/employees.component').then(m => m.Employees) },
      { path: 'users', loadComponent: () => import('./pages/users/users').then(m => m.Users) },
      { path: 'departments', loadComponent: () => import('./pages/departments/departments').then(m => m.Departments) },
      { path: 'reports', loadComponent: () => import('./pages/reports/reports').then(m => m.Reports) },
      { path: 'settings', loadComponent: () => import('./pages/settings/settings').then(m => m.Settings) },
      { path: '', redirectTo: 'employees', pathMatch: 'full' }
    ]
  },
  { path: '', redirectTo: 'login', pathMatch: 'full' }
];
