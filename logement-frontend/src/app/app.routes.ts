import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'logements',
    loadComponent: () =>
      import('./logement/logement.component').then(m => m.LogementComponent),
  },
  {
    path: 'rendezvous',
    loadComponent: () =>
      import('./rendez-vous/rendez-vous.component').then(m => m.RendezVousComponent),
  },
  { path: '', redirectTo: '/logements', pathMatch: 'full' },
  { path: '**', redirectTo: '/logements' },
];
