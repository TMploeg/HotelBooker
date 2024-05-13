import UserService from "../../../services/UserService.js";
import AuthForm from "../AuthForm";

export default function Login({ onLogin }) {
    return <AuthForm title="Login to Account" onSubmit={login} />

    function login(username, password) {
        UserService.login(username, password)
            .then(
                response => {
                    if (onLogin) {
                        onLogin();
                    }
                },
                console.error
            );
    }
}