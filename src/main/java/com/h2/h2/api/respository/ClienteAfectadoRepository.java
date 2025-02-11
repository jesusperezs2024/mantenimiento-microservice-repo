package com.h2.h2.api.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.h2.h2.api.model.ClienteAfectadoModel;
import com.h2.h2.api.model.MantenimientoModel;

@Repository
public interface ClienteAfectadoRepository extends JpaRepository<ClienteAfectadoModel, Long> {
    void deleteByMantenimiento(MantenimientoModel mantenimiento);
}
