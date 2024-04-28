import { useNavigate } from "react-router-dom";
import UserService from "../../../services/UserService.js";
import AuthForm from "../AuthForm";

const TOKEN_STORAGE_LOCATION = 'JWT';
export default function Login() {
    const navigate = useNavigate();

    return <AuthForm title="Login to Account" onSubmit={login} />

    function login(username, password) {
        UserService.login(username, password)
            .then(
                response => {
                    navigate('/');
                },
                console.error
            );
    }
}