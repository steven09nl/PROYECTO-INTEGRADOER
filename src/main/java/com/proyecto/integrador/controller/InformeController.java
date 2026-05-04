package com.proyecto.integrador.controller;

import org.springframework.web.bind.annotation.*;

import com.proyecto.integrador.dto.ApiResponse;
import com.proyecto.integrador.dto.InformeRequest;
import com.proyecto.integrador.entity.Informe;
import com.proyecto.integrador.service.InformeService;

@RestController
@RequestMapping("/api/informes")
@CrossOrigin(origins = "*")
public class InformeController {
    private final InformeService informeService;

    public InformeController(InformeService informeService) {
        this.informeService = informeService;
    }

    @GetMapping
    public ApiResponse listar() {
        return ApiResponse.ok("Listado de informes", informeService.listar());
    }

    @GetMapping("/usuario/{idUsuario}")
    public ApiResponse porUsuario(@PathVariable Long idUsuario) {
        return ApiResponse.ok("Informes por usuario", informeService.listarPorUsuario(idUsuario));
    }

    @PostMapping
    public ApiResponse generar(@RequestBody InformeRequest request) {
        Informe i = new Informe();
        i.setIdUsuarioGen(request.getIdUsuarioGen());
        i.setTipoInforme(request.getTipoInforme());
        i.setFechaGeneracion(request.getFechaGeneracion());
        i.setPeriodo(request.getPeriodo());
        i.setUrlArchivo(request.getUrlArchivo());
        return ApiResponse.ok("Informe generado", informeService.generarInforme(i));
    }

    @GetMapping("/practica/{idPractica}")
    public ApiResponse porPractica(@PathVariable Long idPractica) {
        return ApiResponse.ok("Bitácoras de la práctica", informeService.bitacorasDePractica(idPractica));
    }
}
