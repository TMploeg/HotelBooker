export default function DateTimeSelector({ date, onDateChanged, time, onTimeChanged }) {
    return <div>
        <input name="date" type="date" value={date ?? ''} onChange={event => onDateChanged(event.target.value)} />
        <input name="time" type="time" value={time ?? ''} onChange={event => onTimeChanged(event.target.value)} />
    </div>
}