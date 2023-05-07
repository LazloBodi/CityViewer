import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { AuthService } from 'src/app/services/auth.service';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authServiceMock = jasmine.createSpyObj('AuthService', ['isLoggedIn']);
    authServiceMock.isLoggedIn.and.returnValue(false);
    await TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([]), ReactiveFormsModule],
      declarations: [ LoginComponent ],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
      ],
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
