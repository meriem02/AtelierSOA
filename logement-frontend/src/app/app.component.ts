import { Component } from '@angular/core';
import { RouterModule, RouterOutlet } from '@angular/router';
import { LogementComponent } from "./logement/logement.component";
import { RendezVousComponent } from "./rendez-vous/rendez-vous.component";
import { BrowserModule } from '@angular/platform-browser';
import { routes } from './app.routes';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule],  // PAS RouterModule.forRoot(...)

  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'logement-frontend';


}
