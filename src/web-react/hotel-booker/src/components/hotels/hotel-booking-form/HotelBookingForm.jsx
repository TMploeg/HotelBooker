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
    const [roomCount, setRoomCount] = useState('1');

    const [error, setError] = useState(null);

    const { postBooking } = useBookings();

    const navigate = useNavigate();

    useEffect(validateFields, [checkIn, checkOut, roomCount]);

    return <div className="booking-form-container">
        <div className="booking-form-field">
            <div className="booking-form-field-title">Check In</div>
            <DateTimeSelector className="booking-form-control" value={checkIn} onChanged={setCheckIn} />
        </div>
        <div className="booking-form-field">
            <div className="booking-form-field-title">Check Out</div>
            <DateTimeSelector className="booking-form-control" value={checkOut} onChanged={setCheckOut} />
        </div>
        <div className="booking-form-field">
            <div className="booking-form-field-title">Number of Rooms</div>
            <input
                className="booking-form-control room-count-input"
                type="number"
                min={1}
                max={999}
                value={roomCount}
                onChange={event => setRoomCount(event.target.value)} />
        </div>
        <FlatButton
            disabled={error !== null}
            className="submit-booking-button" onClick={onBookClicked}>Book</FlatButton>
        {error !== null ? <div>{error}</div> : null}
    </div>

    function validateFields() {
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
        else if (roomCount.length === 0) {
            console.log('TEST2');
            setError('Number of rooms must be a number');
        }
    }

    function onBookClicked() {
        if (roomCount.length === 0) {
            console.error('ERR: invalid room count');
            return;
        }

        postBooking(checkIn, checkOut, hotelId, Number(roomCount))
            .then(_ => navigate('/'))
            .catch(error => setError(error.message));
    }
}