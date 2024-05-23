import "./DateTimeSelector.css";

export default function DateTimeSelector({ value, onChanged, ...props }) {
    const dateTime = value instanceof Date ? value : new Date();

    const years = dateTime.getFullYear();
    const month = toMin2LengthString(dateTime.getMonth() + 1);
    const days = toMin2LengthString(dateTime.getDate());
    const hours = toMin2LengthString(dateTime.getHours());
    const minutes = toMin2LengthString(dateTime.getMinutes());

    const date = `${years}-${month}-${days}`;
    const time = `${hours}:${minutes}`;

    const newProps = {
        ...props,
        className: `dt-selector ${props.className ?? ''}`
    }
    return <div {...newProps} >
        <input onKeyDown={e => e.preventDefault()} className="dt-selector-input" name="date" type="date" value={date} onChange={onDateChanged} />
        <input className="dt-selector-input" name="time" type="time" value={time} onChange={onTimeChanged} />
    </div >

    function toMin2LengthString(value) {
        return (value <= 9 ? '0' : '') + value;
    }

    function onDateChanged(event) {
        onChanged(new Date(`${event.target.value}T${time}`));
    }
    function onTimeChanged(event) {
        onChanged(new Date(`${date}T${event.target.value}`));
    }
}