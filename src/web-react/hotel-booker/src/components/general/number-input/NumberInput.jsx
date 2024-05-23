import { useState } from "react";
import "./NumberInput.css";

const FALLBACK_MIN_VALUE = Number.MIN_SAFE_INTEGER;
const FALLBACK_MAX_VALUE = Number.MAX_SAFE_INTEGER;

export default function NumberInput({ value, onChange, min, max, ...props }) {
    const [_, setRerenderFlag] = useState(false);
    const [isNegativeZero, setNegativeZero] = useState(false);

    if (min === undefined || min === null) {
        min = FALLBACK_MIN_VALUE;
    }
    if (max === undefined || max === null) {
        max = FALLBACK_MAX_VALUE;
    }

    return <div className="number-input-container">
        <input {...props} value={value} onChange={event => onValueChanged(event.target.value)} />
        <div className=" number-input-buttons">
            <img
                src="\images\triangle_icon.png"
                onClick={() => onValueChanged(value + 1)} />
            <img
                className="rotate-180deg"
                src="\images\triangle_icon.png"
                onClick={() => onValueChanged(value - 1)} />
        </div>
    </div>


    function onValueChanged(newValue) {
        if (newValue.length === 0) {
            onChange(0);
            return;
        }

        if (newValue === '0-' || newValue === '-') {
            onChange(-0);
            setNegativeZero(true);
            return;
        }

        const parsedValue = Number(newValue);
        if (
            isNaN(parsedValue) ||
            !isFinite(parsedValue) ||
            !Number.isInteger(parsedValue) ||
            parsedValue < min ||
            parsedValue > max
        ) {
            setRerenderFlag(f => !f);
            return;
        }

        onChange(Number(newValue) * (isNegativeZero ? -1 : 1));
        setNegativeZero(false);
    }

    function updateValue(newValue) {

    }
}