import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CityEditComponent } from './city-edit.component';
import { RouterTestingModule } from '@angular/router/testing';
import { CityService } from 'src/app/services/city.service';
import { of } from 'rxjs';
import { City } from 'src/app/models/city.models';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';

describe('CityEditComponent', () => {
  let component: CityEditComponent;
  let fixture: ComponentFixture<CityEditComponent>;
  let cityServiceMock: jasmine.SpyObj<CityService>;
  let authServiceMock: jasmine.SpyObj<AuthService>;
  const testCity: City = { id: 1, name: 'Budapest', photo: 'budapest.jpg' };

  beforeEach(async () => {
    cityServiceMock = jasmine.createSpyObj('CityService', [
      'getCity',
      'updateCity',
    ]);
    cityServiceMock.getCity.and.returnValue(of(testCity));
    cityServiceMock.updateCity.and.returnValue(of(testCity));
    authServiceMock = jasmine.createSpyObj('AuthService', ['hasRole']);
    authServiceMock.hasRole.and.returnValue(true);

    await TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([]), ReactiveFormsModule],
      providers: [
        { provide: CityService, useValue: cityServiceMock },
        { provide: AuthService, useValue: authServiceMock },
      ],
      declarations: [CityEditComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(CityEditComponent);
    component = fixture.componentInstance;
    component.ngOnInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(cityServiceMock.getCity).toHaveBeenCalled();
    expect(component.cityForm.value).toEqual(testCity);
  });

  it('checks name field validity when name is empty', () => {
    let name = component.cityForm.controls['name'];
    name.setValue('');
    let errors = name.errors || {};
    expect(errors['required']).toBeTruthy();
  });

  it('checks name field validity when name is not empty', () => {
    let name = component.cityForm.controls['name'];
    name.setValue('testName');
    let errors = name.errors || {};
    expect(errors['required']).toBeFalsy();
  });

  it('checks phot field validity when phot is empty', () => {
    let photo = component.cityForm.controls['photo'];
    photo.setValue('');
    let errors = photo.errors || {};
    expect(errors['required']).toBeTruthy();
  });

  it('checks phot field validity when phot is not empty', () => {
    let photo = component.cityForm.controls['photo'];
    photo.setValue('testPhoto');
    let errors = photo.errors || {};
    expect(errors['required']).toBeFalsy();
  });

  it('calls cityService.updateCity when Save button clicked', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    (compiled.querySelector('#citySaveButton') as HTMLButtonElement).click();
    expect(cityServiceMock.updateCity).toHaveBeenCalledOnceWith(testCity);
  });
});
