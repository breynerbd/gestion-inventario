export interface CategoryResponse {
    categoryId: number;
    categoryCode: string;
    categoryName: string;
    description: string | null;
    status: string;
    creationDate: string;
    modificationDate: string;
}