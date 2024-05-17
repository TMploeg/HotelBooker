export default function DateTimeSelector({ date, onDateChanged, time, onTimeChanged }) {
    return <div>
        <label>
            date <input type="date" value={date} onChange={event => onDateChanged(event.target.value)} />
        </label>
        <label >
            time <input type="time" value={time} onChange={event => onTimeChanged(event.target.value)} />
        </label>
    </div>
}