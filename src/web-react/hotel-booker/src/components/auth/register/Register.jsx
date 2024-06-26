import useAuthentication from "../../../hooks/useAuthentication";
import AuthForm from "../AuthForm";

export default function Register({ onRegister }) {
    const { register, login } = useAuthentication();

    return <AuthForm
        title="Register Account"
        onSubmit={registerUser}
        validateUsername={validateUsername}
        validatePassword={validatePassword} />

    function registerUser(username, password) {
        return register(username, password).then(
            () => login(username, password).then(() => onRegister?.())
        );
    }

    function validateUsername(username) {
        const errors = [];

        if (username === undefined || username === null || username.length === 0) {
            errors.push('username is required');
        }

        return errors;
    }

    function validatePassword(password) {
        const errors = [];
        const passwordMinLength = 7;
        const specialCharacters = '!@#$%&*()_+=|<>?{}\\[\\]~-';

        if (password === undefined || password === null || password.length === 0) {
            errors.push('password is required');
            return errors;
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
        if (password.length < passwordMinLength) {
            errors.push(`must have at least ${passwordMinLength} characters`);
        }

        return errors;
    }

    function regEx(text, pattern) {
        return text.search(pattern) >= 0;
    }
}