import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { RendezVousService } from '../services/rendez-vous.service';
import { FormsModule } from '@angular/forms';
import { LogementService } from '../services/logement.service';

@Component({
  selector: 'app-rendez-vous',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './rendez-vous.component.html',
  styleUrl: './rendez-vous.component.scss'
})

export class RendezVousComponent implements OnInit {

  rendezVousList: any[] = [];
  logements: any[] = [];

  errorMessage = '';
  selectedRendezVous: any | null = null;
  isAdding = false;
  referenceRecherche: number | null = null;


  constructor(private rendezVousService: RendezVousService, private logementService: LogementService) { }

  ngOnInit(): void {
    this.loadRendezVous();
    this.loadLogements()
  }


  loadLogements(): void {
    this.logementService.getAll().subscribe({
      next: (data: any) => {
        this.logements = data;
      },
      error: err => {
        this.errorMessage = 'Erreur lors du chargement des logements';
        console.error(err);
      }
    });
  }

  loadRendezVous(): void {
    this.rendezVousService.getAll().subscribe({
      next: (data: any) => {
        this.rendezVousList = data;
      },
      error: err => {
        this.errorMessage = 'Erreur lors du chargement des rendez-vous';
        console.error(err);
      }
    });
  }

  rechercherParReference(): void {
    if (this.referenceRecherche === null || this.referenceRecherche === 0) {
      this.loadRendezVous(); // recharge tout si vide
    } else {
      this.rendezVousService.getByLogementReference(this.referenceRecherche).subscribe({
        next: (data: any) => {
          this.rendezVousList = data;
        },
        error: err => {
          this.errorMessage = 'Erreur lors de la recherche par référence';
          console.error(err);
        }
      });
    }
  }


  startAdd(): void {
    this.isAdding = true;
    this.selectedRendezVous = {
      id: null,
      date: '',
      heure: '',
      numTel: '',
      logement: {
        reference: null
      }
    };
  }

  editRendezVous(rdv: any): void {
    this.isAdding = false;
    this.selectedRendezVous = { ...rdv };
  }

  saveRendezVous(): void {
    if (!this.selectedRendezVous) return;

    if (this.isAdding) {
      // Ajouter
      this.rendezVousService.addRendezVous(this.selectedRendezVous).subscribe({
        next: (newRendezVous: any) => {
          // Recharge la liste complète après ajout
          this.loadRendezVous();
          this.selectedRendezVous = null;
          this.isAdding = false;
        },
        error: err => {
          this.errorMessage = 'Erreur lors de l\'ajout du rendez-vous';
          console.error(err);
        }
      });
    } else {
      // Modifier
      this.rendezVousService.updateRendezVous(this.selectedRendezVous.id, this.selectedRendezVous).subscribe({
        next: (responseText) => {
          console.log(responseText);
          // Recharge la liste complète après modification
          this.loadRendezVous();
          this.selectedRendezVous = null;
        },
        error: err => {
          this.errorMessage = 'Erreur lors de la mise à jour';
          console.error(err);
        }
      });
    }
  }


  cancelEdit(): void {
    this.selectedRendezVous = null;
    this.isAdding = false;
  }

  deleteRendezVous(id: number): void {
    if (!confirm('Voulez-vous vraiment supprimer ce rendez-vous ?')) return;

    this.rendezVousService.deleteRendezVous(id).subscribe({
      next: () => {
        this.loadRendezVous();
      },
      error: err => {
        this.errorMessage = 'Erreur lors de la suppression';
        console.error(err);
      }
    });
  }
}