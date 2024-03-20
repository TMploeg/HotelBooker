export abstract class AppRoutes {
    public static readonly HOTELS: string = "hotels";
    public static readonly HOTEL_ID: string = "hotelId";

    public static buildRoute(...args: string[]) {
        return args.join('/');
    }
}