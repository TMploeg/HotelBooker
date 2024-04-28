const API_URL = 'http://localhost:8080/'

export default class ApiService {
    static get(url, params) {
        return new ApiRequest('GET', API_URL + url, params);
    }
}

export class ApiRequest {
    #REQUEST_COMPLETED_STATE = 4;

    #request;
    #responseData;
    #onComplete;

    constructor(httpMethod, url, params) {
        const joinedParams = params === undefined ? undefined : Object.keys(params).map(key => `${key}=${params[key]}`).join('&');

        this.#request = new XMLHttpRequest();
        this.#request.open(httpMethod, url + (joinedParams !== undefined && joinedParams.length > 0 ? `?${joinedParams}` : ''));
        this.#request.setRequestHeader('accept', 'application/json');
        this.#request.onreadystatechange = () => {
            if (!this.#isRequestComplete(this.#request)) {
                return;
            }

            this.#responseData = this.#isRequestSuccesfull(this.#request)
                ? {
                    succes: true,
                    body: JSON.parse(this.#request.responseText)
                }
                : {
                    succes: false,
                    message: this.#request.responsetext
                };

            if (this.#onComplete) {
                this.#onComplete(this.#responseData);
            }
        }

        this.#request.send();
    }

    onComplete(onComplete) {
        if (this.#responseData) {
            onComplete(this.#responseData);
        }
        else {
            this.#onComplete = onComplete;
        }
    }

    #isRequestComplete(request) {
        return request.readyState === this.#REQUEST_COMPLETED_STATE;
    }

    #isRequestSuccesfull(request) {
        return Math.floor(request.status / 100) === 2;
    }
}