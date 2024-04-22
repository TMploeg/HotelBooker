import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxMaterialTimepickerModule } from 'ngx-material-timepicker';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/auth/login/login.component';
import { PasswordHelpDialogComponent } from './components/auth/register/password-help-dialog/password-help-dialog.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { BookingFormComponent } from './components/bookings/booking-form/booking-form.component';
import { BookingInfoComponent } from './components/bookings/booking-info/booking-info.component';
import { BookingListComponent } from './components/bookings/booking-list/booking-list.component';
import { HotelInfoComponent } from './components/hotels/hotel-info/hotel-info.component';
import { HotelListComponent } from './components/hotels/hotel-list/hotel-list.component';
import { MessageBoxComponent } from './components/message-box/message-box.component';
import { AccountMenuComponent } from './components/toolbar/account-menu/account-menu.component';
import { ToolbarComponent } from './components/toolbar/toolbar.component';

@NgModule({
  declarations: [
    AppComponent,
    ToolbarComponent,
    AccountMenuComponent,
    HotelListComponent,
    HotelInfoComponent,
    BookingFormComponent,
    BookingInfoComponent,
    MessageBoxComponent,
    BookingListComponent,
    RegisterComponent,
    LoginComponent,
    PasswordHelpDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    NgxMaterialTimepickerModule,
    MatInputModule,
    MatNativeDateModule,
    MatDialogModule,
    MatSnackBarModule,
    MatSidenavModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
