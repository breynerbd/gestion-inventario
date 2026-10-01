export interface LoginResponse {
    accessToken: string;
    refreshToken: string;
    tokenType: string;
    userId: number;
    username: string;
    roleName: string;
}
