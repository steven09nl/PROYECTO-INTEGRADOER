package com.proyecto.integrador.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.integrador.entity.Bitacora;
import com.proyecto.integrador.entity.Informe;
import com.proyecto.integrador.repository.BitacoraRepository;
import com.proyecto.integrador.repository.InformeRepository;

@Service
public class InformeService {
    private final InformeRepository informeRepository;
    private final BitacoraRepository bitacoraRepository;

    public InformeService(InformeRepository informeRepository, BitacoraRepository bitacoraRepository) {
        this.informeRepository = informeRepository;
        this.bitacoraRepository = bitacoraRepository;
    }

    public Long generarInforme(Informe informe) {
        if (informe.getFechaGeneracion() == null) {
            informe.setFechaGeneracion(LocalDate.now());
        }
        return informeRepository.save(informe);
    }

    public List<Informe> listar() {
        return informeRepository.findAll();
    }

    public List<Informe> listarPorUsuario(Long idUsuario) {
        return informeRepository.findByUsuario(idUsuario);
    }

    public List<Bitacora> bitacorasDePractica(Long idPractica) {
        return bitacoraRepository.findByPractica(idPractica);
    }
}
