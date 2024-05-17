import ApiService from "../services/ApiService";

export default function useBookings() {
    const postBooking = (checkIn, checkOut, hotelId, roomCount) => {
        return ApiService.post(
            'bookings',
            {
                checkIn: checkIn.toISOString(),
                checkOut: checkOut.toISOString(),
                hotelId,
                roomCount
            }
        );
    }

    return {
        postBooking
    };
}