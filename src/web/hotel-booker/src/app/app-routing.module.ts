import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HotelListComponent } from './components/hotels/hotel-list/hotel-list.component';
import { HotelInfoComponent } from './components/hotels/hotel-info/hotel-info.component';
import { AppRoutes } from "./constants/routes";

const routes: Routes = [
  { path: '', redirectTo: 'hotels', pathMatch: 'full' },
  { path: 'hotels', component: HotelListComponent },
  { path: AppRoutes.buildRoute(AppRoutes.HOTELS, ':' + AppRoutes.HOTEL_ID), component: HotelInfoComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
