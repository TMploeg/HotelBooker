export interface NewBookingDTO {
    id: number;
    checkIn: string;
    checkOut: string;
    hotelId: number;
    roomNumbers: number[];
}