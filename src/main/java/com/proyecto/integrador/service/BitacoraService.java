package com.proyecto.integrador.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.integrador.entity.Bitacora;
import com.proyecto.integrador.entity.ControlHoras;
import com.proyecto.integrador.entity.Evidencia;
import com.proyecto.integrador.entity.Observacion;
import com.proyecto.integrador.entity.Respuesta;
import com.proyecto.integrador.repository.BitacoraRepository;
import com.proyecto.integrador.repository.ControlHorasRepository;
import com.proyecto.integrador.repository.EvidenciaRepository;
import com.proyecto.integrador.repository.ObservacionRepository;
import com.proyecto.integrador.repository.RespuestaRepository;

@Service
public class BitacoraService {
    private final BitacoraRepository bitacoraRepository;
    private final ControlHorasRepository controlHorasRepository;
    private final EvidenciaRepository evidenciaRepository;
    private final ObservacionRepository observacionRepository;
    private final RespuestaRepository respuestaRepository;

    public BitacoraService(BitacoraRepository bitacoraRepository,
                           ControlHorasRepository controlHorasRepository,
                           EvidenciaRepository evidenciaRepository,
                           ObservacionRepository observacionRepository,
                           RespuestaRepository respuestaRepository) {
        this.bitacoraRepository = bitacoraRepository;
        this.controlHorasRepository = controlHorasRepository;
        this.evidenciaRepository = evidenciaRepository;
        this.observacionRepository = observacionRepository;
        this.respuestaRepository = respuestaRepository;
    }

    public Long crearBitacora(Bitacora bitacora) {
        return bitacoraRepository.save(bitacora);
    }

    public Bitacora buscarBitacora(Long id) {
        return bitacoraRepository.findById(id);
    }

    public List<Bitacora> listarPorEstudiante(Long idEstudiante) {
        return bitacoraRepository.findByEstudiante(idEstudiante);
    }

    public List<Bitacora> listarPorPractica(Long idPractica) {
        return bitacoraRepository.findByPractica(idPractica);
    }

    public boolean calificar(Long idBitacora, String estado, Double calificacion) {
        return bitacoraRepository.updateStateAndGrade(idBitacora, estado, calificacion);
    }

    public Long registrarHoras(ControlHoras controlHoras) {
        return controlHorasRepository.save(controlHoras);
    }

    public Long agregarEvidencia(Evidencia evidencia) {
        return evidenciaRepository.save(evidencia);
    }

    public Long agregarObservacion(Observacion observacion) {
        return observacionRepository.save(observacion);
    }

    public Long responder(Respuesta respuesta) {
        return respuestaRepository.save(respuesta);
    }

    public List<Respuesta> respuestasDeBitacora(Long idBitacora) {
        return respuestaRepository.findByBitacora(idBitacora);
    }

    public List<ControlHoras> horasDeBitacora(Long idBitacora) {
        return controlHorasRepository.findByBitacora(idBitacora);
    }

    public List<Evidencia> evidenciasDeBitacora(Long idBitacora) {
        return evidenciaRepository.findByBitacora(idBitacora);
    }

    public List<Observacion> observacionesDeBitacora(Long idBitacora) {
        return observacionRepository.findByBitacora(idBitacora);
    }

    public boolean actualizarRetroalimentacion(Long idRespuesta, String retroalimentacion) {
        return respuestaRepository.updateFeedback(idRespuesta, retroalimentacion);
    }
}
