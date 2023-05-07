import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, of } from 'rxjs';
import { Credentials, TokenResponse } from '../models/auth.models';
import jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  readonly loginUrl = 'http://localhost:9876/login';

  constructor(private http: HttpClient) {}

  public login(username: string, password: string): Observable<boolean> {
    return this.http
      .post<TokenResponse>(this.loginUrl, { username, password } as Credentials)
      .pipe(
        map((response: TokenResponse) => {
          localStorage.setItem('token', response.token);
          const roles = (jwt_decode(response.token) as any).roles;
          localStorage.setItem('roles', roles);
          return true;
        })
      );
  }

  public logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('roles');
  }

  public isLoggedIn(): boolean {
    return localStorage.getItem('token') !== null;
  }

  public hasRole(role: string): boolean {
    const roles = localStorage.getItem('roles');
    return roles !== null && roles.split(',').includes(role);
  }
}
