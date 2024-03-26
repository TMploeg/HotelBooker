import { Injectable } from '@angular/core';
import { Time } from '../models/time';
import { SuccesResult, ErrorResult } from '../models/results';

@Injectable({
  providedIn: 'root'
})
export class TimeService {

  constructor() { }

  parseTime(timeString: string): SuccesResult<Time> | ErrorResult {
    if (!timeString) {
      return new ErrorResult(["TimeService:14: input is undefined"]);
    }

    const splitTime: string[] = timeString.split(':');
    if (splitTime.length != 2) {
      return new ErrorResult(["invalid format"]);
    }

    const hours: number = Number.parseInt(splitTime[0]);
    const minutes: number = Number.parseInt(splitTime[1]);

    if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
      return new ErrorResult(["invalid values"]);
    }

    return new SuccesResult(new Time(hours, minutes));
  }
}
