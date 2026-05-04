package com.proyecto.integrador.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.proyecto.integrador.dto.ApiResponse;
import com.proyecto.integrador.dto.PracticeRequest;
import com.proyecto.integrador.dto.QuestionRequest;
import com.proyecto.integrador.entity.Practica;
import com.proyecto.integrador.entity.Pregunta;
import com.proyecto.integrador.service.PracticaService;

@RestController
@RequestMapping("/api/practicas")
@CrossOrigin(origins = "*")
public class PracticaController {
    private final PracticaService practicaService;

    public PracticaController(PracticaService practicaService) {
        this.practicaService = practicaService;
    }

    @GetMapping
    public ApiResponse listar() {
        return ApiResponse.ok("Listado de prácticas", practicaService.listar());
    }

    @GetMapping("/activas")
    public ApiResponse activas() {
        return ApiResponse.ok("Listado de prácticas activas", practicaService.listarActivas());
    }

    @GetMapping("/{id}")
    public ApiResponse buscar(@PathVariable Long id) {
        Practica practica = practicaService.buscar(id);
        return practica == null ? ApiResponse.error("Práctica no encontrada") : ApiResponse.ok("Práctica encontrada", practica);
    }

    @PostMapping
    public ApiResponse crear(@RequestBody PracticeRequest request) {
        Practica p = new Practica();
        p.setNombre(request.getNombre());
        p.setTipoPractica(request.getTipoPractica());
        p.setHorasReglamentarias(request.getHorasReglamentarias());
        p.setEstado(request.getEstado());
        p.setFechaInicio(request.getFechaInicio());
        p.setFechaFin(request.getFechaFin());
        p.setSemestre(request.getSemestre());
        Long id = practicaService.crear(p);
        return ApiResponse.ok("Práctica creada", id);
    }

    @PutMapping("/{id}")
    public ApiResponse actualizar(@PathVariable Long id, @RequestBody PracticeRequest request) {
        Practica p = new Practica();
        p.setIdPractica(id);
        p.setNombre(request.getNombre());
        p.setTipoPractica(request.getTipoPractica());
        p.setHorasReglamentarias(request.getHorasReglamentarias());
        p.setEstado(request.getEstado());
        p.setFechaInicio(request.getFechaInicio());
        p.setFechaFin(request.getFechaFin());
        p.setSemestre(request.getSemestre());
        return practicaService.actualizar(p) ? ApiResponse.ok("Práctica actualizada", true) : ApiResponse.error("No se pudo actualizar");
    }

    @GetMapping("/{id}/preguntas")
    public ApiResponse preguntas(@PathVariable Long id) {
        List<Pregunta> preguntas = practicaService.listarPreguntas(id);
        return ApiResponse.ok("Preguntas de la práctica", preguntas);
    }

    @PostMapping("/{id}/preguntas")
    public ApiResponse agregarPregunta(@PathVariable Long id, @RequestBody QuestionRequest request) {
        Pregunta p = new Pregunta();
        p.setIdPractica(id);
        p.setEnunciado(request.getEnunciado());
        p.setTipoPregunta(request.getTipoPregunta());
        p.setObligatoria(request.getObligatoria());
        p.setOrden(request.getOrden());
        Long savedId = practicaService.agregarPregunta(p);
        return ApiResponse.ok("Pregunta agregada", savedId);
    }

    @PatchMapping("/preguntas/{idPregunta}/orden/{orden}")
    public ApiResponse reordenarPregunta(@PathVariable Long idPregunta, @PathVariable int orden) {
        return practicaService.reordenarPregunta(idPregunta, orden) ? ApiResponse.ok("Pregunta reordenada", true) : ApiResponse.error("No se pudo reordenar");
    }
}
