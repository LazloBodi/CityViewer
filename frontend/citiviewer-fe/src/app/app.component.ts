import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  loggedIn$: Observable<boolean> = this.authService.loggedIn;

  constructor(private authService: AuthService) {}

  onLogout(): void {
    this.authService.logout();
  }
}
