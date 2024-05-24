import axios from "axios";
import useStorage from "./useStorage";

const API_URL = 'http://localhost:8080/';

export default function useApi() {
    const { getJWT } = useStorage();

    const getRequestConfig = (params) => {
        const headers = {
            'accept': 'application/json',
            'Content-Type': 'application/json'
        }

        const authHeader = getAuthHeader();
        if (authHeader !== null) {
            headers['Authorization'] = authHeader;
        }

        return {
            params,
            headers
        };
    }

    const getAuthHeader = () => {
        const token = getJWT()?.token;

        if (token === null) {
            return null;
        }

        return `Bearer ${token}`;
    }

    const get = async (url, params) => {
        return await axios.get(
            API_URL + url,
            getRequestConfig(params)
        );
    }

    const post = async (url, body, params) => {
        return await axios.post(
            API_URL + url,
            body,
            getRequestConfig(params)
        );
    }

    return {
        get,
        post
    };
}