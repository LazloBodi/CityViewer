import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CityListComponent } from './city-list.component';
import { CityService } from 'src/app/services/city.service';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { City, CityPageResponse } from 'src/app/models/city.models';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

describe('CityListComponent', () => {
  let component: CityListComponent;
  let fixture: ComponentFixture<CityListComponent>;
  let cityServiceMock: jasmine.SpyObj<CityService>;
  const testCity1: City = { id: 1, name: 'Budapest', photo: 'budapest.jpg' };
  const testCity2: City = { id: 2, name: 'London', photo: 'london.jpg' };
  const testCityPageResponse: CityPageResponse = {
    cities: [testCity1, testCity2],
    totalCount: 2,
    page: 0,
    size: 20,
  };

  beforeEach(async () => {
    cityServiceMock = jasmine.createSpyObj('CityService', ['getCityPage']);
    cityServiceMock.getCityPage.and.returnValue(of(testCityPageResponse));

    await TestBed.configureTestingModule({
      imports: [FormsModule, NgbModule],
      declarations: [CityListComponent],
      providers: [{ provide: CityService, useValue: cityServiceMock }],
    }).compileComponents();

    fixture = TestBed.createComponent(CityListComponent);
    component = fixture.componentInstance;
    component.ngOnInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(cityServiceMock.getCityPage).toHaveBeenCalledWith(0, 20, '');
    expect(component.cities.length).toEqual(2);
    expect(component.size).toEqual(20);
    expect(component.page).toEqual(1);
    expect(component.totalCount).toEqual(2);
    expect(component.nameSearch).toEqual('');
  });

  it('should handle page change', () => {
    component.onPageChange(2);

    expect(cityServiceMock.getCityPage).toHaveBeenCalledWith(1, 20, '');
    expect(component.cities.length).toEqual(2);
    expect(component.size).toEqual(20);
    expect(component.page).toEqual(2);
    expect(component.totalCount).toEqual(2);
    expect(component.nameSearch).toEqual('');
  });

  it('should handle page resize', () => {
    component.page = 2;

    component.onPageSizeChange(50);

    expect(cityServiceMock.getCityPage).toHaveBeenCalledWith(0, 50, '');
    expect(component.cities.length).toEqual(2);
    expect(component.size).toEqual(50);
    expect(component.page).toEqual(1);
    expect(component.totalCount).toEqual(2);
    expect(component.nameSearch).toEqual('');
  });

  it('should handle name search', () => {
    component.page = 2;
    component.nameSearch = 'Budapest';

    const compiled = fixture.nativeElement as HTMLElement;
    (compiled.querySelector('#nameSearchButton') as HTMLButtonElement).click();

    expect(cityServiceMock.getCityPage).toHaveBeenCalledWith(0, 20, 'Budapest');
    expect(component.cities.length).toEqual(2);
    expect(component.size).toEqual(20);
    expect(component.page).toEqual(1);
    expect(component.totalCount).toEqual(2);
    expect(component.nameSearch).toEqual('Budapest');
  });
});
