export class Time {
    hours: number;
    minutes: number;

    constructor(hours: number, minutes: number) {
        this.hours = hours;
        this.minutes = minutes;
    }

    compareTo(other: Time): number {
        return (
            this.hours > other.hours ? 1 : (
                this.hours < other.hours ? -1 : (
                    this.minutes > other.minutes ? 1 : (
                        this.minutes < other.minutes ? -1 : 0
                    )
                )
            )
        );
    }
}

