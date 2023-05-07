import { TestBed, getTestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';

const testAdminToken =
  'eyJhbGciOiJIUzI1NiJ9.eyJzdWJqZWN0IjoiYWRtaW4iLCJyb2xlcyI6IkFMTE9XX0VESVQiLCJpYXQiOjE2ODM0OTQ0MDYsImV4cCI6MTY4MzQ5ODAwNn0.CfXMQqXUqdbLpUSToVM8T-4nlp6ETbHbAha4oNp2woM';

describe('AuthService', () => {
  let injector: TestBed;
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });
    injector = getTestBed();
    service = injector.inject(AuthService);
    httpMock = injector.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('can call backend login endpoint when login method called', () => {
    const testCredentials = { username: 'testUser', password: 'testPassword' };

    service.login(testCredentials).subscribe((response) => {
      expect(service.isLoggedIn()).toBeTrue();
      expect(service.getToken()).toBe(testAdminToken);
      expect(service.hasRole('ALLOW_EDIT')).toBeTrue();
    });

    const req = httpMock.expectOne(service.loginUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBe(testCredentials);
    req.flush({ token: testAdminToken });
  });
});
