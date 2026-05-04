package com.proyecto.integrador.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.proyecto.integrador.dto.ApiResponse;
import com.proyecto.integrador.dto.BitacoraRequest;
import com.proyecto.integrador.dto.ControlHorasRequest;
import com.proyecto.integrador.dto.EvidenciaRequest;
import com.proyecto.integrador.dto.ObservacionRequest;
import com.proyecto.integrador.dto.RespuestaRequest;
import com.proyecto.integrador.entity.Bitacora;
import com.proyecto.integrador.entity.ControlHoras;
import com.proyecto.integrador.entity.Evidencia;
import com.proyecto.integrador.entity.Observacion;
import com.proyecto.integrador.entity.Respuesta;
import com.proyecto.integrador.service.BitacoraService;

@RestController
@RequestMapping("/api/bitacoras")
@CrossOrigin(origins = "*")
public class BitacoraController {
    private final BitacoraService bitacoraService;

    public BitacoraController(BitacoraService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    @PostMapping
    public ApiResponse crear(@RequestBody BitacoraRequest request) {
        Bitacora b = new Bitacora();
        b.setIdEstudiante(request.getIdEstudiante());
        b.setIdPractica(request.getIdPractica());
        b.setEstado(request.getEstado());
        b.setModalidad(request.getModalidad());
        b.setFechaEnvio(request.getFechaEnvio());
        b.setCalificacion(request.getCalificacion());
        return ApiResponse.ok("Bitácora creada", bitacoraService.crearBitacora(b));
    }

    @GetMapping("/{id}")
    public ApiResponse buscar(@PathVariable Long id) {
        Bitacora b = bitacoraService.buscarBitacora(id);
        return b == null ? ApiResponse.error("Bitácora no encontrada") : ApiResponse.ok("Bitácora encontrada", b);
    }

    @GetMapping("/estudiante/{idEstudiante}")
    public ApiResponse porEstudiante(@PathVariable Long idEstudiante) {
        return ApiResponse.ok("Bitácoras del estudiante", bitacoraService.listarPorEstudiante(idEstudiante));
    }

    @GetMapping("/practica/{idPractica}")
    public ApiResponse porPractica(@PathVariable Long idPractica) {
        return ApiResponse.ok("Bitácoras de la práctica", bitacoraService.listarPorPractica(idPractica));
    }

    @PostMapping("/{idBitacora}/horas")
    public ApiResponse registrarHoras(@PathVariable Long idBitacora, @RequestBody ControlHorasRequest request) {
        ControlHoras control = new ControlHoras();
        control.setIdBitacora(idBitacora);
        control.setFecha(request.getFecha());
        control.setHoraEntrada(request.getHoraEntrada());
        control.setHoraSalida(request.getHoraSalida());
        control.setHorasCumplidas(request.getHorasCumplidas());
        return ApiResponse.ok("Horas registradas", bitacoraService.registrarHoras(control));
    }

    @PostMapping("/{idBitacora}/evidencias")
    public ApiResponse agregarEvidencia(@PathVariable Long idBitacora, @RequestBody EvidenciaRequest request) {
        Evidencia evidencia = new Evidencia();
        evidencia.setIdBitacora(idBitacora);
        evidencia.setUrlArchivo(request.getUrlArchivo());
        evidencia.setFechaCarga(request.getFechaCarga());
        evidencia.setDescripcion(request.getDescripcion());
        return ApiResponse.ok("Evidencia guardada", bitacoraService.agregarEvidencia(evidencia));
    }

    @PostMapping("/{idBitacora}/observaciones")
    public ApiResponse agregarObservacion(@PathVariable Long idBitacora, @RequestBody ObservacionRequest request) {
        Observacion observacion = new Observacion();
        observacion.setIdBitacora(idBitacora);
        observacion.setIdAsesor(request.getIdAsesor());
        observacion.setTexto(request.getTexto());
        observacion.setFecha(request.getFecha());
        return ApiResponse.ok("Observación guardada", bitacoraService.agregarObservacion(observacion));
    }

    @PostMapping("/{idBitacora}/respuestas")
    public ApiResponse responder(@PathVariable Long idBitacora, @RequestBody RespuestaRequest request) {
        Respuesta respuesta = new Respuesta();
        respuesta.setIdBitacora(idBitacora);
        respuesta.setIdPregunta(request.getIdPregunta());
        respuesta.setTextoRespuesta(request.getTextoRespuesta());
        respuesta.setFechaRespuesta(request.getFechaRespuesta());
        respuesta.setRetroalimentacion(request.getRetroalimentacion());
        return ApiResponse.ok("Respuesta guardada", bitacoraService.responder(respuesta));
    }

    @PutMapping("/{idBitacora}/calificar")
    public ApiResponse calificar(@PathVariable Long idBitacora, @RequestParam String estado, @RequestParam Double calificacion) {
        return bitacoraService.calificar(idBitacora, estado, calificacion) ? ApiResponse.ok("Bitácora calificada", true) : ApiResponse.error("No se pudo calificar");
    }

    @GetMapping("/{idBitacora}/respuestas")
    public ApiResponse respuestas(@PathVariable Long idBitacora) {
        List<Respuesta> data = bitacoraService.respuestasDeBitacora(idBitacora);
        return ApiResponse.ok("Respuestas de la bitácora", data);
    }

    @GetMapping("/{idBitacora}/horas")
    public ApiResponse horas(@PathVariable Long idBitacora) {
        return ApiResponse.ok("Control de horas", bitacoraService.horasDeBitacora(idBitacora));
    }

    @GetMapping("/{idBitacora}/evidencias")
    public ApiResponse evidencias(@PathVariable Long idBitacora) {
        return ApiResponse.ok("Evidencias de la bitácora", bitacoraService.evidenciasDeBitacora(idBitacora));
    }

    @GetMapping("/{idBitacora}/observaciones")
    public ApiResponse observaciones(@PathVariable Long idBitacora) {
        return ApiResponse.ok("Observaciones de la bitácora", bitacoraService.observacionesDeBitacora(idBitacora));
    }

    @PatchMapping("/{idBitacora}/retroalimentacion/{idRespuesta}")
    public ApiResponse actualizarRetroalimentacion(@PathVariable Long idBitacora, @PathVariable Long idRespuesta, @RequestParam String texto) {
        return bitacoraService.actualizarRetroalimentacion(idRespuesta, texto)
                ? ApiResponse.ok("Retroalimentación actualizada", true)
                : ApiResponse.error("No se pudo actualizar");
    }
}
