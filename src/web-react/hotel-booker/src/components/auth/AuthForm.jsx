import { useState } from "react";
import FlatButton from "../general/flat-button/FlatButton";
import InputField from "../general/input-field/InputField";
import "./AuthForm.css";

export default function AuthForm({ title, onSubmit, validateUsername, validatePassword }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const usernameErrors = validateUsername ? validateUsername(username) : [];
    const passwordErrors = validatePassword ? validatePassword(password) : [];

    const titleElement = title ? <h1>{title}</h1> : null;

    return <div className="form-container">
        {titleElement}
        <form className="auth-form">
            <InputField
                label="Username"
                value={username}
                onValueChanged={setUsername}
                errors={usernameErrors} />
            <InputField
                label="Password"
                value={password}
                onValueChanged={setPassword}
                errors={passwordErrors}
                toggleVisiblity />
            <FlatButton
                onClick={() => onSubmit(username, password)}
                disabled={usernameErrors.length > 0 || passwordErrors.length > 0}>Submit</FlatButton>
        </form>
    </div>
}