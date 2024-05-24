import useApi from "./useApi";
import useStorage from "./useStorage";

export default function useAuthentication() {
    const { get, post } = useApi();
    const { getJWT, setJWT, removeJWT } = useStorage();

    const login = async (username, password) => {
        const loginResponse = await post('auth/login', {
            username: username,
            password: password
        });

        setJWT({
            token: loginResponse.data.token,
            username: username
        })
    }

    const logout = () => {
        removeJWT();
    }

    const register = async (username, password) => {
        return await post('auth/register', {
            username: username,
            password: password
        });
    }

    const isLoggedIn = async () => {
        const jwt = getJWT();

        if (jwt === undefined) {
            return Promise.resolve({ loggedIn: false });
        }

        return await get('auth/validate-token', { token: jwt.token })
            .then(response => ({ loggedIn: response.data.isValid }));
    }

    const getUsername = () => {
        return getJWT()?.username;
    }

    return {
        login,
        logout,
        register,
        isLoggedIn,
        getUsername
    }
}