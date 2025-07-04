package maria.avila.reniec.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import maria.avila.reniec.service.ReniecService;
import maria.avila.reniec.model.DataReniec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/reniec")
@AllArgsConstructor
public class ReniecRest {

    private final ReniecService reniecService;

    @GetMapping
    public Flux<DataReniec> getAll() {
        return reniecService.getAll();
    }

    @GetMapping("/ruc/{ruc}")
    public Mono<DataReniec> getByRuc(@PathVariable String ruc) {
        return reniecService.fetchAndSaveRucData(ruc);
    }
}
