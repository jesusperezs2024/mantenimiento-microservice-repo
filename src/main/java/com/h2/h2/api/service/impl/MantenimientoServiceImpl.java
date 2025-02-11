package com.h2.h2.api.service.impl;

import com.h2.h2.api.model.ClienteAfectadoModel;
import com.h2.h2.api.model.MantenimientoModel;
import com.h2.h2.api.respository.ClienteAfectadoRepository;
import com.h2.h2.api.respository.MantenimientoRepository;
import com.h2.h2.api.service.MantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.server.ResponseStatusException;
import javax.transaction.Transactional;


@Service
public class MantenimientoServiceImpl implements MantenimientoService {

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    @Autowired
    private ClienteAfectadoRepository clienteAfectadoRepository;

    @Override
    public List<MantenimientoModel> getAll() {
        return mantenimientoRepository.findAll();
    }

    @Override
    public Optional<MantenimientoModel> getById(String id) {
        return Optional.ofNullable(mantenimientoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MANT404: Evento de mantenimiento no encontrado")));
    }

    @Override
    @Transactional
    public MantenimientoModel save(MantenimientoModel mantenimiento) {
        // 🛑 Validar fechas y horas antes de guardar
        if (mantenimiento.getFechaInicio().isAfter(mantenimiento.getFechaFin()) ||
            (mantenimiento.getFechaInicio().isEqual(mantenimiento.getFechaFin()) && 
             mantenimiento.getHoraInicio().compareTo(mantenimiento.getHoraFin()) >= 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MANT002: Fecha/hora fin debe ser posterior a fecha/hora inicio");
        }

        if (mantenimiento.getClientesAfectados().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MANT003: Debe haber al menos un cliente afectado");
        }

        // 🔄 Asignar mantenimiento a los clientes antes de guardar
        mantenimiento.getClientesAfectados().forEach(cliente -> cliente.setMantenimiento(mantenimiento));

        mantenimiento.setFechaCreacion(LocalDateTime.now());
        mantenimiento.setFechaActualizacion(LocalDateTime.now());

        return mantenimientoRepository.save(mantenimiento);
    }

    @Override
    public void deleteById(String id) {
        if (!mantenimientoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MANT404: Evento de mantenimiento no encontrado");
        }
        mantenimientoRepository.deleteById(id);
    }

    @Override
    public List<MantenimientoModel> getByClienteId(String clienteId) {
        List<MantenimientoModel> mantenimientos = mantenimientoRepository.findByClientesAfectados_ClienteId(clienteId);
        if (mantenimientos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "MANT003: Cliente no tiene eventos de mantenimiento registrados");
        }
        return mantenimientos;
    }

    @Override
    @Transactional
    public MantenimientoModel update(String id, MantenimientoModel datosActualizados) {
        MantenimientoModel mantenimiento = mantenimientoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MANT404: Evento de mantenimiento no encontrado"));

        if (mantenimiento.getFechaFin().isBefore(LocalDate.now()) || 
            (mantenimiento.getFechaFin().isEqual(LocalDate.now()) && mantenimiento.getHoraFin().compareTo(datosActualizados.getHoraInicio()) < 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MANT004: Evento no puede ser modificado porque ya finalizó");
        }

        if (datosActualizados.getFechaInicio().isAfter(datosActualizados.getFechaFin()) ||
            (datosActualizados.getFechaInicio().isEqual(datosActualizados.getFechaFin()) && 
             datosActualizados.getHoraInicio().compareTo(datosActualizados.getHoraFin()) >= 0)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "MANT002: Fecha/hora fin debe ser posterior a fecha/hora inicio");
        }

        mantenimiento.setFechaInicio(datosActualizados.getFechaInicio());
        mantenimiento.setHoraInicio(datosActualizados.getHoraInicio());
        mantenimiento.setFechaFin(datosActualizados.getFechaFin());
        mantenimiento.setHoraFin(datosActualizados.getHoraFin());
        mantenimiento.setDescripcion(datosActualizados.getDescripcion());
        mantenimiento.setFechaActualizacion(LocalDateTime.now());

        clienteAfectadoRepository.deleteByMantenimiento(mantenimiento);
        mantenimiento.getClientesAfectados().clear();

        List<ClienteAfectadoModel> nuevosClientes = datosActualizados.getClientesAfectados().stream()
            .map(cliente -> {
                ClienteAfectadoModel nuevoCliente = new ClienteAfectadoModel();
                nuevoCliente.setMantenimiento(mantenimiento);
                nuevoCliente.setClienteId(cliente.getClienteId());
                return nuevoCliente;
            }).collect(Collectors.toList());

        mantenimiento.getClientesAfectados().addAll(nuevosClientes);

        return mantenimientoRepository.save(mantenimiento);
    }
}