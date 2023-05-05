import { Component, OnInit } from '@angular/core';
import { City, CityPageResponse } from 'src/app/models/city.models';
import { CityService } from 'src/app/services/city.service';

@Component({
  selector: 'app-city-list',
  templateUrl: './city-list.component.html',
  styleUrls: ['./city-list.component.scss'],
})
export class CityListComponent implements OnInit {
  cities: City[] = [];
  page: number = 1;
  size: number = 20;
  totalCount: number = 0;

  constructor(private cityService: CityService) {}

  ngOnInit(): void {
    this.loadCities();
  }

  loadCities() {
    this.cityService
      .getCityPage(this.page - 1, this.size)
      .subscribe((page: CityPageResponse) => {
        this.cities = page.cities;
        this.totalCount = page.totalCount;
      });
  }

  onPageChange(page: number) {
    console.log(page);
    if (this.page === page) {
      return;
    }
    this.page = page;
    this.loadCities();
  }
}
