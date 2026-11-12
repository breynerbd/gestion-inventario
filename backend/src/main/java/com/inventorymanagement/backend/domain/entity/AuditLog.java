package com.inventorymanagement.backend.domain.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

    public enum Action {
        CREAR,
        EDITAR,
        ACTIVAR,
        INACTIVAR
    }

    public enum Module {
        CATEGORIAS,
        PROVEEDORES,
        PRODUCTOS,
        MOVIMIENTOS_STOCK
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audit_id")
    private Long auditId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "module", nullable = false, length = 40)
    private Module module;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 30)
    private Action action;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;
}