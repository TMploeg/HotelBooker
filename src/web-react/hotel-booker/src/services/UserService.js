import ApiService from "./ApiService";
import StorageService from "./StorageService";

export default class UserService {
    static login(username, password) {
        return ApiService
            .post('auth/login', {
                username: username,
                password: password
            })
            .then(
                response => Promise.resolve(
                    StorageService.setJWT({
                        token: response.body.token,
                        username: username
                    })
                ),
                error => Promise.reject(error)
            );
    }

    static logout() {
        StorageService.removeJWT();
    }

    static register(username, password) {
        return ApiService
            .post('auth/register', {
                username: username,
                password: password
            });
    }

    static isLoggedIn() {
        const jwt = StorageService.getJWT();

        if (jwt === undefined) {
            return ApiService.emptyPromise(false);
        }

        return ApiService.get('auth/validate-token', { token: jwt.token })
    }

    static getUsername() {
        return StorageService.getJWT()?.username;
    }
}