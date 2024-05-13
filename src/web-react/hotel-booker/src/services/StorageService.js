const JWT_STORAGE_LOCATION = 'JWT';

export default class StorageService {
    static setJWT(jwt) {
        if (jwt === null || jwt === undefined) {
            this.removeJWT();
            return;
        }

        sessionStorage.setItem(JWT_STORAGE_LOCATION, JSON.stringify(jwt));
    }

    static getJWT() {
        const jwt = sessionStorage.getItem(JWT_STORAGE_LOCATION);

        return jwt !== null
            ? JSON.parse(jwt)
            : undefined;
    }

    static removeJWT() {
        sessionStorage.removeItem(JWT_STORAGE_LOCATION);
    }
}