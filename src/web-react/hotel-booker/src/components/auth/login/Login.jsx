import useAuthentication from "../../../hooks/useAuthentication";
import AuthForm from "../AuthForm";

export default function Login({ onLogin }) {
    const { login } = useAuthentication();

    return <AuthForm
        title="Login to Account"
        onSubmit={loginUser}
        validateUsername={validateUsername}
        validatePassword={validatePassword} />

    function loginUser(username, password) {
        return login(username, password).then(() => onLogin?.());
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

        if (password === undefined || password === null || password.length === 0) {
            errors.push('password is required');
        }

        return errors;
    }
}