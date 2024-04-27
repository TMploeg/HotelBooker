import { useState } from "react";
import InputField from "../general/input-field/InputField";
import "./AuthForm.css";

export default function AuthForm({ title, onSubmit }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const titleElement = title ? <h1>{title}</h1> : null;

    return <div className="form-container">
        {titleElement}
        <form className="auth-form">
            <InputField label="Username" value={username} onValueChanged={setUsername} />
            <InputField label="Password" value={password} onValueChanged={setPassword} />
            <button class="submit-button" type="button" onClick={onSubmit}>Submit</button>
        </form>
    </div>
}