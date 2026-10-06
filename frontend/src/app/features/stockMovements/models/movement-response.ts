export interface MovementResponse {
    movementId: number;
    movementType: string;
    productId: number;
    quantity: number;
    referenceDocument: string;
    reason: string;
    userId: number;
    username: string;
    movementDate: string;
    status: string;
}