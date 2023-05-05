import { TestBed, getTestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { CityService } from './city.service';
import { City, CityPageResponse } from '../models/city.models';

describe('CityService', () => {
  let injector: TestBed;
  let service: CityService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CityService],
    });
    injector = getTestBed();
    service = injector.inject(CityService);
    httpMock = injector.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('it can call BEs getCityById endpoint when getCity method called ', () => {
    const testCity: City = { id: 1, name: 'Budapest', photo: 'budapest.jpg' };

    service.getCity(1).subscribe((city) => {
      expect(city).toEqual(testCity);
    });

    const req = httpMock.expectOne(`${service.cityApiUrl}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(testCity);
  });

  it('it can call BEs getCityPage endpoint when getCityPage method called ', () => {
    const testCityPageResponse: CityPageResponse = {
      cities: [{ id: 1, name: 'Budapest', photo: 'budapest.jpg' }],
      totalCount: 1,
      page: 0,
      size: 5,
    };

    service.getCityPage(0, 5, 'Bud').subscribe((page) => {
      expect(page).toEqual(testCityPageResponse);
    });

    const req = httpMock.expectOne(
      `${service.cityApiUrl}?page=0&size=5&nameSearch=Bud`
    );
    expect(req.request.method).toBe('GET');
    req.flush(testCityPageResponse);
  });

  it('it can call BEs updateCity endpoint when updateCity method called ', () => {
    const testCity: City = { id: 1, name: 'Budapest', photo: 'budapest.jpg' };

    service.updateCity(testCity).subscribe((city) => {
      expect(city).toEqual(testCity);
    });

    const req = httpMock.expectOne(`${service.cityApiUrl}/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toBe(testCity);
    req.flush(testCity);
  });
});
