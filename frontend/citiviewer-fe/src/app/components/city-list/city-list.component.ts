import { Component, OnInit } from '@angular/core';
import { City, CityPageResponse } from 'src/app/models/city.models';
import { AuthService } from 'src/app/services/auth.service';
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
  pageSizes: number[] = [10, 20, 50];
  nameSearch: string = '';
  canUserEdit: boolean = false;

  constructor(
    private cityService: CityService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.canUserEdit = this.authService.hasRole('ALLOW_EDIT');
    this.loadCities();
  }

  loadCities() {
    this.cityService
      .getCityPage(this.page - 1, this.size, this.nameSearch)
      .subscribe((page: CityPageResponse) => {
        this.cities = page.cities;
        this.totalCount = page.totalCount;
      });
  }

  onPageChange(page: number) {
    if (this.page === page) {
      return;
    }
    this.page = page;
    this.loadCities();
  }

  onPageSizeChange(size: number) {
    if (this.size === size) {
      return;
    }
    this.size = size;
    this.page = 1;
    this.loadCities();
  }

  onNameSearch() {
    this.page = 1;
    this.loadCities();
  }
}
