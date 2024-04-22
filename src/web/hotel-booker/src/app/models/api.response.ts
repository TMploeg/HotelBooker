import { HttpErrorResponse, HttpResponse } from "@angular/common/http";

export class ApiResponse<TBody> {
    readonly body?: TBody;
    readonly error: any;

    private constructor(body?: TBody, error?: any) {
        this.body = body;
        this.error = error;
    }

    fromHttpResponse<T>(response: HttpResponse<T> | HttpErrorResponse): ApiResponse<T> {
        if (response instanceof HttpErrorResponse) {
            return new ApiResponse<T>(undefined, response.message);
        }

        const body: T = response.body!;
        return new ApiResponse(response.body!);
    }

    static succesResponse<T>(body: T) {
        return new ApiResponse<T>(body);
    }

    static failureResponse<T>(error: any) {
        return new ApiResponse<T>(undefined, error);
    }

    ifSucceeded(succeededFn: (body: TBody) => void) {
        if (this.body) {
            succeededFn(this.body);
        }
    }

    ifSucceededOrElse(succeededFn: (body: TBody) => void, failedFn: (error: any) => void) {
        if (this.body) {
            succeededFn(this.body);
        }
        else {
            failedFn(this.error);
        }
    }

    succeeded(): boolean {
        return this.body != undefined;
    }

    static convertFailureResponse<TOld, TNew>(oldResponse: ApiResponse<TOld>): ApiResponse<TNew> {
        return ApiResponse.failureResponse(oldResponse.error);
    }
}