import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { BookingFormComponent } from './components/bookings/booking-form/booking-form.component';
import { BookingInfoComponent } from './components/bookings/booking-info/booking-info.component';
import { BookingListComponent } from './components/bookings/booking-list/booking-list.component';
import { HotelInfoComponent } from './components/hotels/hotel-info/hotel-info.component';
import { HotelListComponent } from './components/hotels/hotel-list/hotel-list.component';
import { AppRoutes } from "./constants/routes";

const routes: Routes = [
  { path: '', redirectTo: 'bookings', pathMatch: 'full' },
  { path: 'bookings', component: BookingListComponent },
  { path: 'hotels', component: HotelListComponent },
  { path: AppRoutes.buildRoute(AppRoutes.HOTELS, ':' + AppRoutes.HOTEL_ID), component: HotelInfoComponent },
  { path: AppRoutes.buildRoute(AppRoutes.BOOKINGS, AppRoutes.ADD), component: BookingFormComponent },
  { path: AppRoutes.buildRoute(AppRoutes.BOOKINGS, ':' + AppRoutes.BOOKING_ID), component: BookingInfoComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
