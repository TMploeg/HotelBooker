import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Booking } from 'src/app/models/entities/booking';
import { Time } from 'src/app/models/time';
import { TimeService } from 'src/app/services/time.service';

@Component({
  selector: 'app-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.scss']
})
export class BookingFormComponent implements OnInit {
  formGroup!: FormGroup;

  constructor(private timeService: TimeService) { }

  ngOnInit(): void {
    const checkinDateControl: FormControl = new FormControl('', [Validators.required, this.createCheckinDateNotBeforeTodayValidator()]);

    const checkinTimeControl: FormControl = new FormControl('', [Validators.required, this.createCheckinTimeNotBeforeNowValidator()]);
    checkinTimeControl.disable();

    const checkoutDateControl: FormControl = new FormControl('', [Validators.required, this.createCheckoutDateNotBeforeCheckinValidator()]);

    const checkoutTimeControl: FormControl = new FormControl('', [Validators.required, this.createCheckoutTimeNotBeforeCheckinValidator()]);
    checkoutTimeControl.disable();

    this.formGroup = new FormGroup({
      checkinDate: checkinDateControl,
      checkinTime: checkinTimeControl,
      checkoutDate: checkoutDateControl,
      checkoutTime: checkoutTimeControl
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

  submit(): void {
    console.log(this.createBooking(
      'myUsername',
      this.getControl('checkinDate')!.value,
      this.timeService.parseTime(this.getControl('checkinTime')!.value)!,
      this.getControl('checkoutDate')!.value,
      this.timeService.parseTime(this.getControl('checkoutTime')!.value)!
    ));
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

    const controlTime: Time | null = this.timeService.parseTime(control.value);
    const compareTime: Time | null = this.timeService.parseTime(compareTimeString);

    if (controlTime == null || compareTime == null) {
      throw new Error('invalid time format');
    }

    if (controlTime.compareTo(compareTime) < 0) {
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

  private createBooking(
    username: string,
    checkinDate: Date,
    checkinTime: Time,
    checkoutDate: Date,
    checkoutTime: Time
  ): Booking {
    return {
      id: 0,
      username: username,
      checkIn: new Date(
        checkinDate.getFullYear(),
        checkinDate.getMonth(),
        checkinDate.getDate(),
        checkinTime.hours,
        checkinTime.minutes
      ).toISOString(),
      checkOut: new Date(
        checkoutDate.getFullYear(),
        checkoutDate.getMonth(),
        checkoutDate.getDate(),
        checkoutTime.hours,
        checkoutTime.minutes
      ).toISOString(),
    }
  }
}
