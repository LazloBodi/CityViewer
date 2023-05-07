import { TestBed } from '@angular/core/testing';

import { TokenInterceptor } from './token.interceptor';
import { AuthService } from '../services/auth.service';

describe('TokenInterceptor', () => {
  let authServiceMock: jasmine.SpyObj<AuthService>;
  beforeEach(async () => {
    authServiceMock = jasmine.createSpyObj('AuthService', ['getToken']);
    authServiceMock.getToken.and.returnValue('testToken');
    TestBed.configureTestingModule({
      providers: [
        TokenInterceptor,
        { provide: AuthService, useValue: authServiceMock },
      ],
    });
  });

  it('should be created', () => {
    const interceptor: TokenInterceptor = TestBed.inject(TokenInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
