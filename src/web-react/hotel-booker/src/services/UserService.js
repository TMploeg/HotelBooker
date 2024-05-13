import ApiService from "./ApiService";

const TOKEN_STORAGE_LOCATION = 'JWT';

export default class UserService {
    static login(username, password) {
        return ApiService
            .post('auth/login', {
                username: username,
                password: password
            })
            .then(
                response => Promise.resolve(sessionStorage.setItem(TOKEN_STORAGE_LOCATION, response.body.token)),
                error => Promise.reject(error)
            );
    }

    static logout() {
        sessionStorage.removeItem(TOKEN_STORAGE_LOCATION);
    }

    static register(username, password) {
        return ApiService
            .post('auth/register', {
                username: username,
                password: password
            });
    }

    static isLoggedIn() {
        return sessionStorage.getItem(TOKEN_STORAGE_LOCATION) !== null;
    }
}