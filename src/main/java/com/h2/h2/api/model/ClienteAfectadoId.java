package com.h2.h2.api.model;

import java.io.Serializable;
import java.util.Objects;

public class ClienteAfectadoId implements Serializable {
    private String mantenimiento; // evento_id
    private String clienteId;

    public ClienteAfectadoId() {}

    public ClienteAfectadoId(String mantenimiento, String clienteId) {
        this.mantenimiento = mantenimiento;
        this.clienteId = clienteId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClienteAfectadoId that = (ClienteAfectadoId) obj;
        return Objects.equals(mantenimiento, that.mantenimiento) &&
               Objects.equals(clienteId, that.clienteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mantenimiento, clienteId);
    }
}