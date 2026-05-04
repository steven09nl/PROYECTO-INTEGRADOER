package com.proyecto.integrador.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.integrador.entity.Practica;
import com.proyecto.integrador.entity.Pregunta;
import com.proyecto.integrador.repository.PracticaRepository;
import com.proyecto.integrador.repository.PreguntaRepository;

@Service
public class PracticaService {
    private final PracticaRepository practicaRepository;
    private final PreguntaRepository preguntaRepository;

    public PracticaService(PracticaRepository practicaRepository, PreguntaRepository preguntaRepository) {
        this.practicaRepository = practicaRepository;
        this.preguntaRepository = preguntaRepository;
    }

    public List<Practica> listar() {
        return practicaRepository.findAll();
    }

    public List<Practica> listarActivas() {
        return practicaRepository.findActive();
    }

    public Practica buscar(Long id) {
        return practicaRepository.findById(id);
    }

    public Long crear(Practica practica) {
        return practicaRepository.save(practica);
    }

    public boolean actualizar(Practica practica) {
        return practicaRepository.update(practica);
    }

    public List<Pregunta> listarPreguntas(Long idPractica) {
        return preguntaRepository.findByPractica(idPractica);
    }

    public Long agregarPregunta(Pregunta pregunta) {
        return preguntaRepository.save(pregunta);
    }

    public boolean reordenarPregunta(Long idPregunta, int orden) {
        return preguntaRepository.reorder(idPregunta, orden);
    }
}
