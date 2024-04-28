import { useState } from "react";
import "./InputField.css";

export default function InputField({ label, value, onValueChanged, type }) {
    const [inputHasFocus, setInputHasFocus] = useState(false);
    const labelElement = label
        ? <label className={`field-label${inputHasFocus || value.length > 0 ? ' shifted' : ''}`}>{label}</label>
        : null;

    return <div className={`field-container${inputHasFocus ? ' active' : ''}`}>
        <input
            className="field-input"
            value={value}
            onChange={event => onValueChanged(event.target.value)}
            onFocus={() => setInputHasFocus(true)}
            onBlur={() => setInputHasFocus(false)}
            type={type ?? 'text'} />
        {labelElement}
    </div>
}