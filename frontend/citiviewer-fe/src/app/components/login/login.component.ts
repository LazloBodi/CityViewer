import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Credentials } from 'src/app/models/auth.models';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loading: boolean = true;
  loginError: boolean = false;
  loginForm!: FormGroup;
  credentials: Credentials = { username: '', password: '' };

  constructor(
    private authSerice: AuthService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.authSerice.isLoggedIn()) {
      this.router.navigate(['']);
    }
    this.loginForm = this.formBuilder.group({
      username: [this.credentials.username, Validators.required],
      password: [this.credentials.password, Validators.required],
    });
    this.loading = false;
  }

  onLogin() {
    this.loading = true;
    console.log(this.loginForm.value);
    this.authSerice.login(this.loginForm.value).subscribe({
      next: () => {
        this.loading = false;
        this.loginError = false;
        this.router.navigate(['']);
      },
      error: (error) => {
        console.log(error);
        this.loading = false;
        this.loginError = true;
      },
    });
  }
}
