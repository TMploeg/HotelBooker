import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AppRoutes } from 'src/app/constants/routes';
import { Time } from 'src/app/models/time';
import { BookingService } from 'src/app/services/booking.service';
import { TimeService } from 'src/app/services/time.service';
import { MessageBoxComponent } from '../../message-box/message-box.component';
import { ErrorResult, SuccesResult } from '../../../models/results';

@Component({
  selector: 'app-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.scss']
})
export class BookingFormComponent implements OnInit {
  formGroup!: FormGroup;

  private hotelId!: number;

  constructor(
    private bookingService: BookingService,
    private timeService: TimeService,
    private router: Router,
    private dialog: MatDialog
  ) {
    const hotelId: number = Number.parseInt(this.router.getCurrentNavigation()?.extras.state!['hotelId']);

    if (Number.isNaN(hotelId)) {
      console.log('invalid hotel id');
    }

    this.hotelId = hotelId;
  }

  ngOnInit(): void {
    const checkinDateControl: FormControl = new FormControl('', [Validators.required, this.createCheckinDateNotBeforeTodayValidator()]);

    const checkinTimeControl: FormControl = new FormControl('', [Validators.required, this.createCheckinTimeNotBeforeNowValidator()]);
    checkinTimeControl.disable();

    const checkoutDateControl: FormControl = new FormControl('', [Validators.required, this.createCheckoutDateNotBeforeCheckinValidator()]);

    const checkoutTimeControl: FormControl = new FormControl('', [Validators.required, this.createCheckoutTimeNotBeforeCheckinValidator()]);
    checkoutTimeControl.disable();

    const roomCountControl: FormControl = new FormControl(1)

    this.formGroup = new FormGroup({
      checkinDate: checkinDateControl,
      checkinTime: checkinTimeControl,
      checkoutDate: checkoutDateControl,
      checkoutTime: checkoutTimeControl,
      roomCount: roomCountControl
    });
  }

  submit(): void {
    const parsedCheckinTime: SuccesResult<Time> | ErrorResult = this.timeService.parseTime(this.getControl('checkinTime')!.value);
    if (parsedCheckinTime instanceof ErrorResult) {
      throw new Error("check-in time is in invalid format");
    }
    const checkIn: string = this.createDateTimeString(
      this.getControl('checkinDate')!.value,
      parsedCheckinTime.getValue()
    );

    const parsedCheckoutTime: SuccesResult<Time> | ErrorResult = this.timeService.parseTime(this.getControl('checkoutTime')!.value);
    if (parsedCheckoutTime instanceof ErrorResult) {
      throw new Error("check-out time is in invalid format");
    }
    const checkOut: string = this.createDateTimeString(
      this.getControl('checkoutDate')!.value,
      parsedCheckoutTime.getValue()
    );

    const nrOfRooms: number = this.getControl('roomCount')!.value;

    this.bookingService
      .placeBooking(this.hotelId, checkIn, checkOut, nrOfRooms)
      .subscribe(response => {
        if (response instanceof SuccesResult) {
          this.router.navigate([AppRoutes.BOOKINGS, response.getValue().id]);
          return;
        }

        console.log(response.getErrors());
        this.dialog.open(MessageBoxComponent, {
          data: response.getErrors()
        })
      });
  }

  getControl(controlName: string): FormControl | null {
    if (!this.formGroup) {
      return null;
    }

    return this.formGroup.controls[controlName] as FormControl;
  }

  controlHasValue(controlName: string): boolean {
    return this.formGroup.controls[controlName].value;
  }

  updateControlValidation(controlName: string): void {
    this.formGroup.controls[controlName].updateValueAndValidity();
  }

  setControlDisabled(controlName: string, disabled: boolean): void {
    const control: FormControl = this.formGroup.controls[controlName] as FormControl;

    if (disabled) {
      control.disable();
    }
    else {
      control.enable();
    }
  }

  controlHasError(controlName: string, error: string): boolean {
    const control: FormControl | null = this.getControl(controlName);

    if (!control) {
      return false;
    }

    return control.hasError(error);
  }

  controlIsDisabled(controlName: string) {
    const control: FormControl | null = this.getControl(controlName);

    if (!control) {
      return false;
    }

    return control.disabled;
  }

  checkinDateChanged() {
    this.setControlDisabled('checkinTime', !this.controlHasValue('checkinDate'));

    this.updateControlValidation('checkinTime');

    this.updateControlValidation('checkoutDate');
    this.updateControlValidation('checkoutTime');
  }

  checkinTimeChanged(newTime: string) {
    this.getControl('checkinTime')?.setValue(newTime);
    this.updateControlValidation('checkoutTime');
  }

  checkoutDateChanged() {
    this.setControlDisabled('checkoutTime', !this.controlHasValue('checkoutDate'));

    this.updateControlValidation('checkoutTime');
  }

  private createCheckinDateNotBeforeTodayValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      return this.dateControlValueBefore(control, new Date()) ? { beforeToday: true } : null;
    };
  }

  private createCheckoutDateNotBeforeCheckinValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      return this.dateControlValueBefore(control, this.getControl('checkinDate')?.value) ? { beforeCheckin: true } : null;
    }
  }

  private createCheckinTimeNotBeforeNowValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const checkinDateControl: FormControl | null = this.getControl('checkinDate');

      if (!checkinDateControl) {
        return null;
      }

      const now: Date = new Date();

      const dateComparison = this.compareDates(checkinDateControl.value, now);
      if (dateComparison != 0) {
        return dateComparison > 0 ? null : { beforeCheckin: true }
      }

      const nowTimeString: string = now.getHours() + ':' + now.getMinutes();

      return this.timeControlValueBefore(control, nowTimeString) ? { beforeNow: true } : null;
    }
  }

  private createCheckoutTimeNotBeforeCheckinValidator() {
    return (control: AbstractControl): ValidationErrors | null => {
      const checkoutDateControl: FormControl | null = this.getControl('checkoutDate');
      const checkinDateControl: FormControl | null = this.getControl('checkinDate');

      if (!checkinDateControl || !checkoutDateControl) {
        return null;
      }

      const dateComparison = this.compareDates(checkoutDateControl.value, checkinDateControl.value);
      if (dateComparison != 0) {
        return dateComparison > 0 ? null : { beforeCheckin: true }
      }

      const checkinTimeControl: FormControl | null = this.getControl('checkinTime');
      if (!checkinTimeControl) {
        return null;
      }

      return this.timeControlValueBefore(control, checkinTimeControl.value) ? { beforeCheckin: true } : null;
    }
  }

  private dateControlValueBefore(control: AbstractControl, compareDate: Date): boolean {
    if (!this.formGroup || !control.value || !compareDate) {
      return false;
    }

    return this.compareDates(control.value, compareDate) >= 0 ? false : true;
  }

  private timeControlValueBefore(control: AbstractControl, compareTimeString: string): boolean {
    if (!control.value) {
      return false;
    }

    const controlTime: SuccesResult<Time> | ErrorResult = this.timeService.parseTime(control.value);
    const compareTime: SuccesResult<Time> | ErrorResult = this.timeService.parseTime(compareTimeString);

    if (controlTime instanceof ErrorResult || compareTime instanceof ErrorResult) {
      throw new Error('invalid time format');
    }

    if (controlTime.getValue().compareTo(compareTime.getValue()) < 0) {
      return true;
    }

    return false;
  }

  private compareDates(date1: Date, date2: Date): number {
    const formattedDate1: Date = new Date(date1.toDateString());
    const formattedDate2: Date = new Date(date2.toDateString());

    return (
      formattedDate1 > formattedDate2 ? 1 : (
        formattedDate1 < formattedDate2 ? -1 : 0
      )
    );
  }

  private createDateTimeString(date: Date, time: Time) {
    const dateTimeString: string = new Date(
      date.getFullYear(),
      date.getMonth(),
      date.getDate(),
      time.hours,
      time.minutes
    ).toISOString();

    return dateTimeString.slice(0, dateTimeString.length - 1);
  }
}
