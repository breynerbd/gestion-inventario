export interface ProductResponse {
    productId: number;
    productCode: string;
    productName: string;
    description: string;
    categoryId: number;
    supplierId: number;
    unitOfMeasure: string;
    purchasePrice: number;
    salePrice: number;
    currentStock: number;
    minimumStock: number;
    maximumStock: number;
    status: string;
}