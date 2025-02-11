package com.h2.h2.api.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.h2.h2.api.model.MantenimientoModel;


@Repository
public interface MantenimientoRepository extends JpaRepository<MantenimientoModel, String> {
    List<MantenimientoModel> findByClientesAfectados_ClienteId(String clienteId);
}

