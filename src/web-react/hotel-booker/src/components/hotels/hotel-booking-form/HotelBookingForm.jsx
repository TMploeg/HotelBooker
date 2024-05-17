import { useState } from "react";
import DateTimeSelector from "../../general/date-time-selector/DateTimeSelector";
import "./HotelBookingForm.css";

export default function HotelBookingForm({ hotelName }) {
    const currentDateTime = getCurrentDateTime();

    const [checkin, setCheckin] = useState(currentDateTime);
    const [checkout, setCheckout] = useState(currentDateTime);

    console.log('CURRENT DT', currentDateTime);
    console.log('CHECKIN', checkin);
    console.log('CHECKOUT', checkout);

    return <div className="booking-form-container">
        <div className="dt-select-container">
            <div className="dt-select-title">Check In</div>
            <DateTimeSelector
                date={checkin.date} onDateChanged={newDate => stateChanged('date', newDate, setCheckin)}
                time={checkin.time} onTimeChanged={newTime => stateChanged('time', newTime, setCheckin)}
            />
        </div>
        <div className="dt-select-container">
            <div className="dt-select-title">Check Out</div>
            <DateTimeSelector
                date={checkout.date} onDateChanged={newDate => stateChanged('date', newDate, setCheckout)}
                time={checkout.time} onTimeChanged={newTime => stateChanged('time', newTime, setCheckout)} />
        </div>
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

        const month = (now.getMonth() <= 9 ? '0' : '') + (now.getMonth() + 1);

        const date = `${now.getFullYear()}-${month}-${now.getDate()}`;
        const time = `${now.getHours()}:${now.getMinutes()}`;

        return {
            date: date,
            time: time
        }
    }
}