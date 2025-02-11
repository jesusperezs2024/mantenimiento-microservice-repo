package com.h2.h2.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.h2.h2.api.model.MantenimientoModel;
import com.h2.h2.api.service.MantenimientoService;


@RestController
@RequestMapping("/api/v1/mantenimiento")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @GetMapping
    public ResponseEntity<List<MantenimientoModel>> getAll() {
        return ResponseEntity.ok(mantenimientoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MantenimientoModel> getById(@PathVariable String id) {
        Optional<MantenimientoModel> mantenimiento = mantenimientoService.getById(id);
        return mantenimiento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<MantenimientoModel>> getByClienteId(@PathVariable String clienteId) {
        return ResponseEntity.ok(mantenimientoService.getByClienteId(clienteId));
    }

    @PostMapping
    public ResponseEntity<MantenimientoModel> create(@RequestBody MantenimientoModel mantenimiento) {
        return ResponseEntity.ok(mantenimientoService.save(mantenimiento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MantenimientoModel> update(@PathVariable String id, @RequestBody MantenimientoModel mantenimiento) {
        return ResponseEntity.ok(mantenimientoService.update(id, mantenimiento));
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!mantenimientoService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        mantenimientoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}