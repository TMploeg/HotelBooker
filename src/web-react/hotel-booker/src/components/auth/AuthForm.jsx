import { useState } from "react";
import FlatButton from "../general/flat-button/FlatButton";
import IconButton from "../general/icon-button";
import InputField from "../general/input-field/InputField";
import "./AuthForm.css";

export default function AuthForm({ title, onSubmit }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [passwordVisible, setPasswordVisible] = useState(false);

    const titleElement = title ? <h1>{title}</h1> : null;

    return <div className="form-container">
        {titleElement}
        <form className="auth-form">
            <div className="input-field-container">
                <InputField label="Username" value={username} onValueChanged={setUsername} />
            </div>
            <div className="input-field-container">
                <InputField
                    label="Password"
                    value={password}
                    onValueChanged={setPassword}
                    type={passwordVisible ? 'text' : 'password'} />
                <div className="suffix">
                    <IconButton
                        onClick={() => setPasswordVisible(visible => !visible)}
                        imgUrl={`/images/visibility_${passwordVisible ? 'off' : 'on'}.png`} />
                </div>
            </div>
            <FlatButton onClick={() => onSubmit(username, password)}>Submit</FlatButton>
        </form>
    </div>
}