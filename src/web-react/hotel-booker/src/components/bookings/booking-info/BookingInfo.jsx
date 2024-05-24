import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ClipLoader } from "react-spinners";
import useApi from "../../../hooks/useApi";
import useCSSProperties from "../../../hooks/useCSSProperties";

export default function BookingInfo() {
    const { id } = useParams();
    const { getProperty } = useCSSProperties();
    const { get } = useApi();

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
                        <div>Address: {displayAddress(booking.hotel.address)}</div>
                    </div>
                </>
        }
    </div>

    function loadBooking() {
        get(`bookings/${id}`)
            .catch(_ => leave())
            .then(response => setBooking(response.data));
    }

    function leave() {
        navigate('/bookings')
    }

    function displayDate(date) {
        if (!(date instanceof Date)) {
            return;
        }

        return date.toLocaleString('default', {
            month: 'long',
            day: 'numeric',
            year: 'numeric',
            hour: 'numeric',
            minute: 'numeric'
        })
    }

    function displayAddress(address) {
        return `${address.street} ${address.houseNumber}, ${address.city}`;
    }
}