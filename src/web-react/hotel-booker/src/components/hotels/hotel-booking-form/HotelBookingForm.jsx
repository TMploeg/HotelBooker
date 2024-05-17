import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useBookings from "../../../hooks/useBookings";
import DateTimeSelector from "../../general/date-time-selector/DateTimeSelector";
import FlatButton from "../../general/flat-button";
import "./HotelBookingForm.css";

export default function HotelBookingForm() {
    const initialCheckIn = new Date();
    initialCheckIn.setDate(initialCheckIn.getDate() + 1);

    const initialCheckOut = new Date(initialCheckIn);
    initialCheckOut.setHours(initialCheckOut.getHours() + 1);

    const hotelId = Number(useParams().id);

    const [checkIn, setCheckIn] = useState(initialCheckIn);
    const [checkOut, setCheckOut] = useState(initialCheckOut);

    const [error, setError] = useState(null);

    const { postBooking } = useBookings();

    const navigate = useNavigate();

    useEffect(validateCheckInCheckOut, [checkIn, checkOut]);

    return <div className="booking-form-container">
        <div className="dt-select-container">
            <div className="dt-select-title">Check In</div>
            <DateTimeSelector className="dt-select-component" value={checkIn} onChanged={setCheckIn} />
        </div>
        <div className="dt-select-container">
            <div className="dt-select-title">Check Out</div>
            <DateTimeSelector className="dt-select-component" value={checkOut} onChanged={setCheckOut} />
        </div>
        <FlatButton
            disabled={error !== null}
            className="submit-booking-button" onClick={onBookClicked}>Book</FlatButton>
        {error !== null ? <div>{error}</div> : null}
    </div>

    function validateCheckInCheckOut() {
        setError(null);

        if (isNaN(checkIn.getTime())) {
            console.warn('checkIn is invalid');
            setCheckIn(new Date());
        }
        if (isNaN(checkOut.getTime())) {
            console.warn('checkOut is invalid');
            setCheckOut(new Date());
        }

        if (checkIn >= checkOut) {
            setError('Check out must be after check in');
        }
    }

    function onBookClicked() {
        postBooking(checkIn, checkOut, hotelId, 1)
            .then(_ => navigate('/'))
            .catch(error => setError(error.message));
    }
}