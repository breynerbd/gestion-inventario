export interface ProductCreate {
    productCode: string;
    productName: string;
    description: string;
    categoryId: number;
    supplierId: number;
    unitOfMeasure: string;
    purchasePrice: number;
    salePrice: number;
    minimumStock: number;
    maximumStock: number;
}