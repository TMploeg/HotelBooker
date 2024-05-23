import { useNavigate } from "react-router-dom";
import "./BookingListItem.css";

export default function BookingListItem({ booking }) {
    const navigate = useNavigate();

    const checkIn = new Date(booking.checkIn);

    return <div className="booking-list-item-container" onClick={() => navigate(`/bookings/${booking.id}`)}>
        <div>
            <div className="booking-checkin-date">{checkIn.toLocaleString('default', { month: 'short', day: 'numeric', year: 'numeric' })}</div>
            <div className="booking-checkin-time">{checkIn.toLocaleString('default', { hour: '2-digit', minute: '2-digit' })}</div>
        </div>
        <div>
            <div className="booking-hotel-name">{booking.hotel.name}</div>
            <div className="booking-hotel-address">{booking.hotel.address}</div>
        </div>
    </div>
}