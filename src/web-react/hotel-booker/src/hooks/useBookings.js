import useApi from "./useApi";

export default function useBookings() {
    const { post } = useApi();

    const postBooking = async (checkIn, checkOut, hotelId, roomCount) => {
        const requestData = {
            checkIn: checkIn.toISOString(),
            checkOut: checkOut.toISOString(),
            hotelId,
            roomCount
        };

        return await post('bookings', requestData);
    }

    return {
        postBooking
    };
} 