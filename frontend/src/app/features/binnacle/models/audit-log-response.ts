export interface AuditLogResponse {
    auditId: number;
    username: string;
    module: string;
    recordId: number;
    action: string;
    occurredAt: string;
}
