import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ClipLoader } from "react-spinners";
import useCSSProperties from "../../../hooks/useCSSProperties";
import ApiService from "../../../services/ApiService";

export default function BookingInfo() {
    const { id } = useParams();
    const { getProperty } = useCSSProperties();

    const navigate = useNavigate();
    if (id === undefined) {
        leave();
    }

    const [booking, setBooking] = useState(null);
    useEffect(loadBooking, []);

    return <div>
        {
            booking === undefined || booking === null
                ? <ClipLoader color={getProperty('--default-color')} />
                : <>
                    <h1>Booking at {booking.hotel.name}</h1>
                    <div>
                        <div>Check In: {displayDate(new Date(booking.checkIn))}</div>
                        <div>Check Out: {displayDate(new Date(booking.checkOut))}</div>
                        <div>Address: {booking.hotel.address}</div>
                    </div>
                </>
        }
    </div>

    function loadBooking() {
        ApiService.get(`bookings/${id}`)
            .catch(_ => leave())
            .then(response => setBooking(response.body));
    }

    function leave() {
        navigate('/bookings')
    }

    function displayDate(date) {
        if (!(date instanceof Date)) {
            return;
        }

        return date.toLocaleString('default', { month: 'long', day: 'numeric', year: 'numeric', hour: 'numeric', minute: 'numeric' })
    }
}