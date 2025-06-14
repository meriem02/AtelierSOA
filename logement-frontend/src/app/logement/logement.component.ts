import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { LogementService } from '../services/logement.service';

interface Logement {
  reference: number;
  adresse: string;
  delegation: string;
  gouvernorat: string;
  type: string;
  description: string;
  prix: number;
}

@Component({
  selector: 'app-logement',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './logement.component.html',
  styleUrls: ['./logement.component.scss']
})
export class LogementComponent implements OnInit {

  logements: Logement[] = [];
  errorMessage = '';

  selectedLogement: Logement | null = null;
  isAdding = false;  // Pour différencier ajout / édition
  delegationRecherche: string = '';
  referenceRecherche: number | null = null;


  constructor(private logementService: LogementService) { }

  ngOnInit() {
    this.loadLogements();
  }


  rechercher(): void {
    // Si les deux champs sont vides, on charge tout
    if ((!this.delegationRecherche || this.delegationRecherche.trim() === '') &&
      (this.referenceRecherche === null || this.referenceRecherche === 0)) {
      this.loadLogements();
      return;
    }

    // Si on a seulement la référence
    if ((this.referenceRecherche !== null && this.referenceRecherche !== 0) &&
      (!this.delegationRecherche || this.delegationRecherche.trim() === '')) {
      this.logementService.getByReference(this.referenceRecherche).subscribe({
        next: (data: any) => {
          this.logements = Array.isArray(data) ? data : [data];
        },
        error: err => {
          this.errorMessage = 'Erreur lors de la recherche par référence';
          this.logements = [];
          console.error(err);
        }
      });
      return;
    }

    // Si on a seulement la délégation
    if ((this.delegationRecherche && this.delegationRecherche.trim() !== '') &&
      (this.referenceRecherche === null || this.referenceRecherche === 0)) {
      this.logementService.getByDelegation(this.delegationRecherche).subscribe({
        next: (data: any) => {
          this.logements = data;
        },
        error: err => {
          this.errorMessage = 'Erreur lors de la recherche par délégation';
          this.logements = [];
          console.error(err);
        }
      });
      return;
    }

    // Si on a les deux : délégation ET référence, on va filtrer côté front (car pas d'API combinée)
    // On récupère d'abord par délégation, puis on filtre par référence côté client
    this.logementService.getByDelegation(this.delegationRecherche).subscribe({
      next: (data: any) => {
        this.logements = data.filter((l: Logement) => l.reference === this.referenceRecherche);
      },
      error: err => {
        this.errorMessage = 'Erreur lors de la recherche combinée';
        this.logements = [];
        console.error(err);
      }
    });
  }

  clearSearch(): void {
    this.referenceRecherche = null;
    this.delegationRecherche = '';
    this.loadLogements();
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


  // Démarrer l’ajout d’un nouveau logement
  startAdd(): void {
    this.isAdding = true;
    this.selectedLogement = {
      reference: 0,
      adresse: '',
      delegation: '',
      gouvernorat: '',
      type: '',
      description: '',
      prix: 0,
    };
  }

  // Modifier un logement existant
  editLogement(logement: Logement): void {
    this.isAdding = false;
    this.selectedLogement = { ...logement };
  }

  saveLogement(): void {
    if (!this.selectedLogement) return;

    if (this.isAdding) {
      // Ajouter
      this.logementService.addLogement(this.selectedLogement).subscribe({
        next: (newLogement: any) => {
          // Recharge la liste complète après ajout
          this.loadLogements();
          this.selectedLogement = null;
          this.isAdding = false;
        },
        error: err => {
          this.errorMessage = 'Erreur lors de l\'ajout';
          console.error(err);
        }
      });
    } else {
      // Modifier
      this.logementService.updateLogement(this.selectedLogement.reference, this.selectedLogement).subscribe({
        next: (responseText) => {
          console.log(responseText);
          // Recharge la liste complète après modification
          this.loadLogements();
          this.selectedLogement = null;
        },
        error: err => {
          this.errorMessage = 'Erreur lors de la mise à jour';
          console.error(err);
        }
      });
    }
  }


  cancelEdit(): void {
    this.selectedLogement = null;
    this.isAdding = false;
  }

  deleteLogement(reference: number): void {
    if (!confirm('Voulez-vous vraiment supprimer ce logement ?')) return;

    this.logementService.deleteLogement(reference).subscribe({
      next: () => {
        // Recharge la liste complète après suppression
        this.loadLogements();
      },
      error: err => {
        this.errorMessage = 'Erreur lors de la suppression';
        console.error(err);
      }
    });
  }

}
