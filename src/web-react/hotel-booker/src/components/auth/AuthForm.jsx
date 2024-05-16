import { useState } from "react";
import { BarLoader } from "react-spinners";
import useCSSProperties from "../../hooks/useCSSProperties";
import FlatButton from "../general/flat-button/FlatButton";
import InputField from "../general/input-field/InputField";
import "./AuthForm.css";

const ENTER_KEY_CODE = 'Enter';

export default function AuthForm({ title, onSubmit, validateUsername, validatePassword }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const [loading, setLoading] = useState(false);
    const [formError, setFormError] = useState(null);

    const { getProperty } = useCSSProperties();

    const usernameErrors = validateUsername ? validateUsername(username) : [];
    const passwordErrors = validatePassword ? validatePassword(password) : [];
    const submitDisabled = usernameErrors.length > 0 || passwordErrors.length > 0;

    const titleElement = title ? <h1>{title}</h1> : null;

    const formInfoElement = formError && formError !== null
        ? formError
        : loading
            ? <BarLoader color={getProperty('--default-color')} />
            : undefined;

    return <div className="form-container">
        {titleElement}
        <form className="auth-form">
            <InputField
                label="Username"
                value={username}
                onValueChanged={setUsername}
                errors={usernameErrors}
                onKeyUp={onKeyUp} />
            <InputField
                label="Password"
                value={password}
                onValueChanged={setPassword}
                errors={passwordErrors}
                toggleVisiblity
                onKeyUp={onKeyUp} />
            <FlatButton
                onClick={submit}
                disabled={submitDisabled}>Submit</FlatButton>
        </form>
        {formInfoElement ? <div className="form-info">{formInfoElement}</div> : null}
    </div>

    function submit() {
        if (submitDisabled) {
            return;
        }

        setLoading(true);
        setFormError(null);
        onSubmit(username, password)
            .catch(error => setFormError(error.message))
            .finally(() => setLoading(false));
    }

    function onKeyUp(event) {
        if (event.code === ENTER_KEY_CODE) {
            submit();
        }
    }
}