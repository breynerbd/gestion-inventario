export interface UserCreate {
    username: string;
    password: string;
    firstNames: string;
    lastNames: string;
    email: string;
    phone: string;
    roleId: number;
}