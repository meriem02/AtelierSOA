import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RendezVousService {

  private readonly apiUrl = 'http://localhost:8088/LogementRendezVous_Etudiant_war_exploded/api/rendezvous';

  private readonly httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  };

  constructor(private readonly http: HttpClient) { }

  getAll() {
    return this.http.get(`${this.apiUrl}/getAll`, this.httpOptions);
  }

  getById(id: number) {
    return this.http.get(`${this.apiUrl}/getById/${id}`, this.httpOptions);
  }

  getByLogementReference(reference: number) {
    return this.http.get(`${this.apiUrl}/getByLogementReference/${reference}`, this.httpOptions);
  }

  addRendezVous(rendezVous: any) {
    return this.http.post(`${this.apiUrl}/add`, rendezVous, this.httpOptions);
  }

  updateRendezVous(id: number, rendezVous: any) {
    return this.http.put(`${this.apiUrl}/update/${id}`, rendezVous, this.httpOptions);
  }

  deleteRendezVous(id: number) {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, this.httpOptions);
  }
}
