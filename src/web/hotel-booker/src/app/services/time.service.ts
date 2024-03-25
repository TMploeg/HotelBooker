import { Injectable } from '@angular/core';
import { Time } from '../models/time';

@Injectable({
  providedIn: 'root'
})
export class TimeService {

  constructor() { }

  parseTime(timeString: string): Time | null {
    if (!timeString) {
      return null;
    }

    const splitTime: string[] = timeString.split(':');
    if (splitTime.length != 2) {
      return null;
    }

    const hours: number = Number.parseInt(splitTime[0]);
    const minutes: number = Number.parseInt(splitTime[1]);

    if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
      return null;
    }

    return new Time(hours, minutes);
  }
}
