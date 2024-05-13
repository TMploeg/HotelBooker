import { useState } from "react";
import ConditionalElement from "../conditional-element";
import IconButton from "../icon-button";
import "./InputField.css";

export default function InputField({ label, value, onValueChanged, errors, toggleVisiblity }) {
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
        <input
            className="field-input"
            value={value}
            onChange={event => onValueChanged(event.target.value)}
            onFocus={handleFocus}
            onBlur={handleBlur}
            type={visible ? 'text' : 'password'} />
        <ConditionalElement condition={hasLabel}>
            <label className={`field-label${inputHasFocus || value.length > 0 ? ' shifted' : ''}`}>{label}</label>
        </ConditionalElement>
        <div className="buttons-container">
            <ConditionalElement condition={hasError}>
                <IconButton
                    imgUrl="/images/help.png"
                    onClick={showErrors} />
            </ConditionalElement>
            <ConditionalElement condition={toggleVisiblity}>
                <IconButton
                    imgUrl={`/images/visibility_${visible ? 'off' : 'on'}.png`}
                    onClick={() => setVisible(visible => !visible)} />
            </ConditionalElement>
        </div>
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