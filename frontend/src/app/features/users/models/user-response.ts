export interface UserResponse {
    userId: number;
    username: string;
    password: string;
    firstNames: string;
    lastNames: string;
    email: string;
    phone: string;
    roleId: number;
    roleName: string;
    status: string;
    failedAttempts: number;
}