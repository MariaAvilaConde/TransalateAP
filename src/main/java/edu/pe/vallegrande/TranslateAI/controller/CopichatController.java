package edu.pe.vallegrande.TranslateAI.controller;

import edu.pe.vallegrande.TranslateAI.model.Copichat;
import edu.pe.vallegrande.TranslateAI.service.CopichatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/copichat")
@RequiredArgsConstructor
public class CopichatController {

    private final CopichatService service;

    // Crear una nueva consulta
    @PostMapping("/crear")
    public Mono<ResponseEntity<Copichat>> crear(@RequestBody String pregunta) {
        return service.crearConsulta(pregunta)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    // Editar una consulta existente
    @PutMapping("/editar/{id}")
    public Mono<ResponseEntity<Copichat>> editar(@PathVariable Long id, @RequestBody String nuevaPregunta) {
        return service.editarConsulta(id, nuevaPregunta)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }

    // Eliminar físico
    @DeleteMapping("/eliminar-fisico/{id}")
    public Mono<ResponseEntity<Void>> eliminarFisico(@PathVariable Long id) {
        return service.eliminarFisico(id)
                .thenReturn(ResponseEntity.noContent().build());
    }

    // Eliminar lógico
    @PutMapping("/eliminar-logico/{id}")
    public Mono<ResponseEntity<Copichat>> eliminarLogico(@PathVariable Long id) {
        return service.eliminarLogico(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Restaurar una consulta eliminada lógicamente
    @PutMapping("/restaurar/{id}")
    public Mono<ResponseEntity<Copichat>> restaurar(@PathVariable Long id) {
        return service.restaurar(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Listar todos (incluye inactivos)
    @GetMapping("/listar-todo")
    public Flux<Copichat> listarTodo() {
        return service.listarTodo();
    }

    // Listar solo activos
    @GetMapping("/listar-activos")
    public Flux<Copichat> listarActivos() {
        return service.listarActivos();
    }
}
