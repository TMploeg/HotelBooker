export abstract class AppRoutes {
    public static readonly HOTELS: string = "hotels";
    public static readonly HOTEL_ID: string = "hotelId";
    public static readonly BOOK: string = 'book';
    public static readonly BOOKINGS: string = 'bookings';
    public static readonly ADD: string = 'add';
    public static readonly BOOKING_ID: string = 'bookingId';

    public static buildRoute(...args: string[]) {
        return args.join('/');
    }
}