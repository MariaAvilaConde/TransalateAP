package edu.pe.vallegrande.TranslateAI.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.pe.vallegrande.TranslateAI.model.Copichat;
import edu.pe.vallegrande.TranslateAI.repository.CopichatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CopichatService {

    private final CopichatRepository repository;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Mono<Copichat> crearConsulta(String pregunta) {
        if (pregunta == null || pregunta.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("La pregunta no puede estar vacía."));
        }

        return obtenerRespuestaAPI(pregunta.trim())
                .flatMap(respuesta -> {
                    Copichat c = new Copichat(null, pregunta.trim(), respuesta, LocalDate.now(), 'A');
                    return repository.save(c);
                });
    }

    public Mono<String> obtenerRespuestaAPI(String pregunta) {
        String body;
        try {
            body = String.format("""
            {
                "message": %s,
                "conversation_id": null,
                "markdown": true
            }
            """, objectMapper.writeValueAsString(pregunta)); // Escapa bien el string
        } catch (Exception e) {
            return Mono.error(e);
        }

       HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://copilot5.p.rapidapi.com/copilot"))
                .header("x-rapidapi-key", "c213f1fbadmsh32f92d765ad949fp18cc40jsna12a61342bf1")
                .header("x-rapidapi-host", "copilot5.p.rapidapi.com")
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return Mono.fromCallable(() -> {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            
            // Convertir la respuesta JSON en un Map para extraer el mensaje
            Map<String, Object> responseBody = objectMapper.readValue(response.body(), Map.class);
            Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
            return (String) data.get("message");  // Extraer solo el mensaje
        });
    }

    public Mono<Copichat> editarConsulta(Long id, String nuevaPregunta) {
        if (nuevaPregunta == null || nuevaPregunta.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("La nueva pregunta no puede estar vacía."));
        }

        return obtenerRespuestaAPI(nuevaPregunta.trim())
                .flatMap(nuevaRespuesta -> 
                        repository.findById(id)
                                .flatMap(c -> {
                                    c.setPregunta(nuevaPregunta.trim());
                                    c.setRespuesta(nuevaRespuesta);
                                    return repository.save(c);
                                })
                );
    }

    public Mono<Void> eliminarFisico(Long id) {
        return repository.deleteById(id);
    }

    public Mono<Copichat> eliminarLogico(Long id) {
        return repository.findById(id)
                .flatMap(c -> {
                    c.setStatus('I');
                    return repository.save(c);
                });
    }

    public Mono<Copichat> restaurar(Long id) {
        return repository.findById(id)
                .flatMap(c -> {
                    c.setStatus('A');
                    return repository.save(c);
                });
    }

    public Flux<Copichat> listarTodo() {
        return repository.findAll();
    }

    public Flux<Copichat> listarActivos() {
        return repository.findByStatus('A');
    }
}
