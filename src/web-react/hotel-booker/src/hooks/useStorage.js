const JWT_STORAGE_LOCATION = 'JWT';

export default function useStorage() {
    const setJWT = (jwt) => {
        if (jwt === null || jwt === undefined) {
            this.removeJWT();
            return;
        }

        sessionStorage.setItem(JWT_STORAGE_LOCATION, JSON.stringify(jwt));
    }

    const getJWT = () => {
        const jwt = sessionStorage.getItem(JWT_STORAGE_LOCATION);

        return jwt !== null
            ? JSON.parse(jwt)
            : undefined;
    }

    const removeJWT = () => {
        sessionStorage.removeItem(JWT_STORAGE_LOCATION);
    }

    return {
        setJWT,
        getJWT,
        removeJWT
    }
}