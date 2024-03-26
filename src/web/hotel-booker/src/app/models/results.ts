export class SuccesResult<TValue> {
    constructor(private value: TValue) { }

    getValue() {
        return this.value;
    }
}

export class ErrorResult {
    constructor(private errors: string[]) { }

    getErrors() {
        return this.errors;
    }
}