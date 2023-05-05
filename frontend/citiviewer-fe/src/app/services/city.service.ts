import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { City, CityPageResponse } from '../models/city.models';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class CityService {
  private cityApiUrl = 'http://localhost:9876/api/v1/city';

  constructor(private http: HttpClient) {}

  public getCity(id: number): Observable<City> {
    return this.http.get<City>(`${this.cityApiUrl}/${id}`);
  }

  public getCityPage(page: number, size: number): Observable<CityPageResponse> {
    return this.http.get<CityPageResponse>(
      `${this.cityApiUrl}?page=${page}&size=${size}`
    );
  }

  public updateCity(city: City): Observable<City> {
    return this.http.put<City>(`${this.cityApiUrl}/${city.id}`, city);
  }
}
