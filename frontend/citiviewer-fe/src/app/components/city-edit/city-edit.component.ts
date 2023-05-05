import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CityService } from 'src/app/services/city.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-city-edit',
  templateUrl: './city-edit.component.html',
  styleUrls: ['./city-edit.component.scss'],
})
export class CityEditComponent implements OnInit {
  loading: boolean = true;
  cityForm!: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private cityService: CityService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    const id = +(this.route.snapshot.paramMap.get('id') || 0);
    this.cityService.getCity(id).subscribe((city) => {
      this.cityForm = this.formBuilder.group({
        id: [city.id, Validators.required],
        name: [city.name, Validators.required],
        photo: [city.photo, Validators.required],
      });
      this.loading = false;
    });
  }

  onSubmit() {
    this.loading = true;
    this.cityService.updateCity({ ...this.cityForm.value }).subscribe(
      (city) => {
        this.cityForm.patchValue({ ...city });
        this.loading = false;
      },
      (error) => {
        console.log(error);
        this.loading = false;
      }
    );
  }
}
