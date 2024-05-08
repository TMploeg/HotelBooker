import { useState } from "react";
import FlatButton from "../general/flat-button/FlatButton";
import IconButton from "../general/icon-button";
import InputField from "../general/input-field/InputField";
import "./AuthForm.css";

export default function AuthForm({ title, onSubmit }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [passwordVisible, setPasswordVisible] = useState(false);

    const usernameErrors = getUsernameErrors();
    const passwordErrors = getPasswordErrors();

    const titleElement = title ? <h1>{title}</h1> : null;

    return <div className="form-container">
        {titleElement}
        <form className="auth-form">
            <div className="input-field-container">
                <div className="error-container">
                    {usernameErrors.map((error, index) => <div key={index}>{error}</div>)}
                </div>
                <InputField
                    label="Username"
                    value={username}
                    onValueChanged={setUsername}
                    hasError={usernameErrors.length > 0} />
            </div>
            <div className="input-field-container">
                <div className="error-container">
                    {passwordErrors.map((error, index) => <div key={index}>{error}</div>)}
                </div>
                <InputField
                    label="Password"
                    value={password}
                    onValueChanged={setPassword}
                    type={passwordVisible ? 'text' : 'password'}
                    hasError={passwordErrors.length > 0} />
                <div className="suffix">
                    <IconButton
                        onClick={() => setPasswordVisible(visible => !visible)}
                        imgUrl={`/images/visibility_${passwordVisible ? 'off' : 'on'}.png`} />
                </div>
            </div>
            <FlatButton onClick={() => onSubmit(username, password)}>Submit</FlatButton>
        </form>
    </div>

    function getUsernameErrors() {
        const errors = [];
        if (username.length === 0) {
            errors.push('username is required');
        }
        return errors;
    }

    function getPasswordErrors() {
        const errors = [];
        const passwordMinLength = 7;
        const specialCharacters = '!@#$%&*()_+=|<>?{}\\[\\]~-';

        if (password.length < passwordMinLength) {
            errors.push(`must have at least ${passwordMinLength} characters`);
        }
        if (!regEx(password, '[a-z]')) {
            errors.push('must have lowercase letter');
        }
        if (!regEx(password, '[A-Z]')) {
            errors.push('must have uppercase letter');
        }
        if (!regEx(password, '[0-9]')) {
            errors.push('must have digit');
        }
        if (!regEx(password, `[${specialCharacters}]`)) {
            errors.push('must have special character');
        }

        return errors;
    }

    function regEx(text, pattern) {
        return text.search(pattern) >= 0;
    }
}