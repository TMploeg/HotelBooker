export interface ApiResponse<TBody> {
    body: TBody | null;
    error: any;
    succeeded: boolean;
}