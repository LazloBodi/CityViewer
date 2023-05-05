import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CityListComponent } from './components/city-list/city-list.component';
import { CityEditComponent } from './components/city-edit/city-edit.component';

const routes: Routes = [
  { path: '', redirectTo: 'cities', pathMatch: 'full' },
  { path: 'cities', component: CityListComponent },
  { path: 'city/:id', component: CityEditComponent },
  { path: '**', redirectTo: 'cities' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
