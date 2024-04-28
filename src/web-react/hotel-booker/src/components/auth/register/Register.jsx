import { useNavigate } from "react-router-dom";
import UserService from "../../../services/UserService";
import AuthForm from "../AuthForm";

export default function Register() {
    const navigate = useNavigate();

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