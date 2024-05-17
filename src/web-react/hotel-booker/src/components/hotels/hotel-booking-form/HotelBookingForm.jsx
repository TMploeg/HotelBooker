import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ApiService from "../../../services/ApiService";
import DateTimeSelector from "../../general/date-time-selector/DateTimeSelector";
import FlatButton from "../../general/flat-button";
import "./HotelBookingForm.css";

export default function HotelBookingForm() {
    const currentDateTime = getCurrentDateTime();

    const hotelId = Number(useParams().id);

    const [checkIn, setCheckIn] = useState(currentDateTime);
    const [checkOut, setCheckOut] = useState(currentDateTime);

    const [error, setError] = useState(null);

    useEffect(validateCheckInCheckOut, [checkIn, checkOut]);

    return <div className="booking-form-container">
        <div className="dt-select-container">
            <div className="dt-select-title">Check In</div>
            <DateTimeSelector
                date={checkIn.date} onDateChanged={newDate => stateChanged('date', newDate, setCheckIn)}
                time={checkIn.time} onTimeChanged={newTime => stateChanged('time', newTime, setCheckIn)} />
        </div>
        <div className="dt-select-container">
            <div className="dt-select-title">Check Out</div>
            <DateTimeSelector
                date={checkOut.date} onDateChanged={newDate => stateChanged('date', newDate, setCheckOut)}
                time={checkOut.time} onTimeChanged={newTime => stateChanged('time', newTime, setCheckOut)} />
        </div>
        <FlatButton onClick={postBooking}>Book</FlatButton>
        {error !== null ? <div>{error}</div> : null}
    </div>

    function stateChanged(propertyName, propertyValue, stateSetter) {
        stateSetter(value => {
            const newValue = { ...value };

            newValue[propertyName] = propertyValue;

            return newValue;
        });
    }

    function getCurrentDateTime() {
        const now = new Date();

        const years = now.getFullYear();
        const month = toMin2LengthString(now.getMonth() + 1);
        const days = toMin2LengthString(now.getDate());
        const hours = toMin2LengthString(now.getHours());
        const minutes = toMin2LengthString(now.getMinutes());

        const date = `${years}-${month}-${days}`;
        const time = `${hours}:${minutes}`;

        return {
            date: date,
            time: time
        }
    }

    function validateCheckInCheckOut() {
        setError(null);

        const checkInDT = new Date(`${checkIn.date}T${checkIn.time}`);
        const checkOutDT = new Date(`${checkOut.date}T${checkOut.time}`);

        if (checkInDT > checkOutDT) {
            setError('Check out cannot be before check in');
        }
    }

    function postBooking() {
        const roomCount = 1;
        const body = {
            checkIn: `${checkIn.date}T${checkIn.time}`,
            checkOut: `${checkOut.date}T${checkOut.time}`,
            hotelId,
            roomCount
        };

        console.log(body);

        ApiService.post('bookings', body)
            .catch(console.error)
            .finally(console.log);
    }

    function toMin2LengthString(value) {
        return (value <= 9 ? '0' : '') + value;
    }
}