import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, map, of } from 'rxjs';
import { Credentials, TokenResponse } from '../models/auth.models';
import jwt_decode from 'jwt-decode';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  readonly loginUrl = 'http://localhost:9876/login';

  public loggedIn = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient, private router: Router) {
    this.loggedIn.next(this.isLoggedIn());
  }

  public login(credentials: Credentials): Observable<void> {
    return this.http.post<TokenResponse>(this.loginUrl, credentials).pipe(
      map((response: TokenResponse) => {
        localStorage.setItem('token', response.token);
        const roles = (jwt_decode(response.token) as any).roles;
        localStorage.setItem('roles', roles);
        this.loggedIn.next(true);
      })
    );
  }

  public logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('roles');
    this.loggedIn.next(false);
    this.router.navigate(['/login']);
  }

  public isLoggedIn(): boolean {
    return localStorage.getItem('token') !== null;
  }

  public hasRole(role: string): boolean {
    const roles = localStorage.getItem('roles');
    return roles !== null && roles.split(',').includes(role);
  }

  public getToken(): string | null {
    return localStorage.getItem('token');
  }
}
