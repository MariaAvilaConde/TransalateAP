package maria.avila.reniec.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import maria.avila.reniec.model.DataReniec;
import  maria.avila.reniec.repository.ReniecRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ReniecService {

    private static final String RENIEC_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im1hcmlhLmF2aWxhQHZhbGxlZ3JhbmRlLmVkdS5wZSJ9.i8HTp6A8ENt2wxuu2NyEvyyw_VZSsvmiaXr7eG0ltgA";
    private final WebClient webClient;
    private final ReniecRepository reniecRepository;

    public ReniecService(WebClient.Builder webClientBuilder, ReniecRepository reniecRepository) {
        this.webClient = webClientBuilder.baseUrl("https://dniruc.apisperu.com/api/v1").build();
        this.reniecRepository = reniecRepository;
    }

    public Flux<DataReniec> getAll() {
        return reniecRepository.findAll();
    }

    public Mono<DataReniec> fetchAndSaveRucData(String ruc) {
        String url = "/ruc/" + ruc + "?token=" + RENIEC_TOKEN;

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(DataReniec.class)
                .doOnNext(data -> log.info("Datos obtenidos de SUNAT: {}", data))
                .flatMap(reniecRepository::save)
                .doOnError(error -> {
                    if (error instanceof WebClientResponseException ex) {
                        log.error("Error al consumir API SUNAT: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
                    } else {
                        log.error("Error desconocido: ", error);
                    }
                });
    }
}