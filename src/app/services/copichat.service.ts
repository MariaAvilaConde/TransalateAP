import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Copichat } from '../models/copichat.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CopichatService {

  private apiUrl = 'https://special-fishstick-pj67v9w7q7crr69-8086.app.github.dev/api/copichat';

  constructor(private http: HttpClient) {}

  crear(pregunta: string): Observable<Copichat> {
    return this.http.post<Copichat>(`${this.apiUrl}/crear`, pregunta, {
      headers: { 'Content-Type': 'text/plain' }
    });
  }

  editar(id: number, nuevaPregunta: string): Observable<Copichat> {
    return this.http.put<Copichat>(`${this.apiUrl}/editar/${id}`, nuevaPregunta, {
      headers: { 'Content-Type': 'text/plain' }
    });
  }

  eliminarFisico(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/eliminar-fisico/${id}`);
  }

  eliminarLogico(id: number): Observable<Copichat> {
    return this.http.put<Copichat>(`${this.apiUrl}/eliminar-logico/${id}`, {});
  }

  restaurar(id: number): Observable<Copichat> {
    return this.http.put<Copichat>(`${this.apiUrl}/restaurar/${id}`, {});
  }

  listarTodo(): Observable<Copichat[]> {
    return this.http.get<Copichat[]>(`${this.apiUrl}/listar-todo`);
  }

  listarActivos(): Observable<Copichat[]> {
    return this.http.get<Copichat[]>(`${this.apiUrl}/listar-activos`);
  }
}
