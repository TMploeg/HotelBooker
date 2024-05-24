import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { ClipLoader } from "react-spinners";
import useApi from "../../../hooks/useApi";
import useCSSProperties from "../../../hooks/useCSSProperties";
import "./BookingList.css";
import BookingListItem from "./booking-list-item";

export default function BookingList() {
    const navigate = useNavigate();
    const { getProperty } = useCSSProperties();
    const { get } = useApi();

    const [bookings, setBookings] = useState(null);
    useEffect(loadBookings, []);

    return <div>
        <h1>My Bookings</h1>
        <div className="booking-list">
            {
                bookings !== undefined && bookings !== null
                    ? bookings.length > 0
                        ? bookings.map(b => <BookingListItem key={b.id} booking={b} />)
                        : 'no bookings found'
                    : <ClipLoader color={getProperty('--default-color')} />
            }
        </div>
    </div>

    function loadBookings() {
        get('bookings')
            .catch(_ => navigate('/'))
            .then(response => setBookings(response.data));
    }
}

