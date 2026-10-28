export interface MovementResponse {
    movementId: number;
    movementType: string;
    productId: number;
    productName: string;
    quantity: number;
    referenceDocument: string;
    reason: string;
    userId: number;
    username: string;
    movementDate: string;
    status: string;
    modificationDate: string;
}