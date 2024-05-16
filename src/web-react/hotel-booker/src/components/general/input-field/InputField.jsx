import { useState } from "react";
import IconButton from "../icon-button";
import "./InputField.css";

export default function InputField({ label, value, onValueChanged, errors, toggleVisiblity, onKeyUp }) {
    const [inputHasFocus, setInputHasFocus] = useState(false);
    const [touched, setTouched] = useState(false);
    const [visible, setVisible] = useState(!toggleVisiblity);

    const hasLabel = label !== undefined && label !== null && label.length > 0;
    if (!hasLabel) {
        label = 'field';
    }
    const hasError = errors !== undefined && errors !== null && errors.length > 0 && touched;
    const extraClassName = inputHasFocus
        ? 'active'
        : (hasError)
            ? 'error'
            : '';

    return <div className={`field-container ${extraClassName}`}>
        <div className={`input-container ${extraClassName}`}>
            <input
                className="field-input"
                value={value}
                onChange={event => onValueChanged(event.target.value)}
                onFocus={handleFocus}
                onBlur={handleBlur}
                type={visible ? 'text' : 'password'}
                onKeyUp={onKeyUp} />
            {hasLabel ? <label className={`field-label${inputHasFocus || value.length > 0 ? ' shifted' : ''}`}>{label}</label> : null}
            <div className="buttons-container">
                {toggleVisiblity
                    ? <IconButton
                        imgUrl={`/images/visibility_${visible ? 'off' : 'on'}.png`}
                        onClick={() => setVisible(visible => !visible)} />
                    : null
                }
            </div>
        </div>
        <div className="field-error">{hasError ? errors[0] : null}</div>
    </div>

    function handleFocus() {
        setInputHasFocus(true);
    }

    function handleBlur() {
        setInputHasFocus(false);
        setTouched(true);
    }

    function showErrors() {
        alert([`${label} is invalid`, ...(errors.map(e => `- ${e}`))].join('\n'));
    }
}