import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LogementService {

  private readonly apiUrl = 'http://localhost:8088/LogementRendezVous_Etudiant_war_exploded/api/logement';

  private readonly httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  };

  constructor(private readonly http: HttpClient) { }

  getAll() {
    return this.http.get(`${this.apiUrl}/getAll`, this.httpOptions);
  }

  getByReference(reference: number) {
    return this.http.get(`${this.apiUrl}/getByReference/${reference}`, this.httpOptions);
  }

  getByDelegation(delegation: string) {
    return this.http.get(`${this.apiUrl}/getByDelegation/${delegation}`, this.httpOptions);
  }

  addLogement(logement: any) {
    return this.http.post(`${this.apiUrl}/add`, logement, this.httpOptions);
  }

  updateLogement(reference: number, logement: any) {
    return this.http.put(`${this.apiUrl}/update/${reference}`, logement, this.httpOptions);
  }

  deleteLogement(reference: number) {
    return this.http.delete(`${this.apiUrl}/delete/${reference}`, this.httpOptions);
  }
}
