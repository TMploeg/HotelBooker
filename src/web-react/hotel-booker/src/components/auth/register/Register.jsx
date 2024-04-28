import UserService from "../../../services/UserService";
import AuthForm from "../AuthForm";

export default function Register() {
    return <AuthForm title="Register Account" onSubmit={register} />

    function register(username, password) {
        UserService.register(username, password).then(
            response => {
                UserService.login(username, password).then(response => {
                    navigate('/');
                });
            },
            console.error
        );
    }
}