package com.h2.h2.api.service;



import java.util.List;
import java.util.Optional;

import com.h2.h2.api.model.MantenimientoModel;

public interface MantenimientoService {
    List<MantenimientoModel> getAll();
    MantenimientoModel update(String id, MantenimientoModel mantenimiento);
    Optional<MantenimientoModel> getById(String id);
    MantenimientoModel save(MantenimientoModel mantenimiento);
    void deleteById(String id);
    List<MantenimientoModel> getByClienteId(String clienteId);
}

