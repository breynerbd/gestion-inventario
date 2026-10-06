export interface MovementCreate {
    movementType: string;
    productId: number;
    quantity: number;
    referenceDocument: string;
    reason: string;
}