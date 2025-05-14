package edu.pe.vallegrande.TranslateAI.repository;

import edu.pe.vallegrande.TranslateAI.model.Copichat;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface CopichatRepository extends ReactiveCrudRepository<Copichat, Long> {
    Flux<Copichat> findByStatus(char status);
}
