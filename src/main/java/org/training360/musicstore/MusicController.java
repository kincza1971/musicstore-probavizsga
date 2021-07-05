package org.training360.musicstore;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/instruments")
public class MusicController {

    private MusicService service;

    public MusicController(MusicService service) {
        this.service = service;
    }

    @PostMapping()
    public InstrumentDTO addInstrument(@Valid @RequestBody CreateInstrumentCommand command) {
        return service.addInstrument(command);
    }

    @GetMapping()
    public List<InstrumentDTO> getInstruments(@RequestParam Optional<String> brand, @RequestParam Optional<Integer> price) {
        return service.getInstruments(brand,price);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllInstruments() {
        service.deleteAllInstruments();
    }

    @GetMapping("/{id}")
    public InstrumentDTO findInstrumentById(@PathVariable Long id) {
        return service.findInstrumentById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void InstrumentDTO(@PathVariable Long id) {
        service.deleteInstrumentById(id);
    }

    @PutMapping("/{id}")
    public InstrumentDTO updatePriceById(@PathVariable Long id, @RequestBody UpdatePriceCommand command) {
        return service.updatePriceById(id,command);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Problem> handleNotFound(IllegalArgumentException exception) {
        Problem problem = Problem.builder()
                .withType(URI.create("instruments/not-found"))
                .withTitle("Not found")
                .withStatus(Status.NOT_FOUND)
                .withDetail(exception.getMessage())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleValidException(MethodArgumentNotValidException exception) {
        List<Violation> violations =
                exception.getBindingResult().getFieldErrors().stream()
                        .map(fe -> new Violation(fe.getField(), fe.getDefaultMessage()))
                        .collect(Collectors.toList());

        Problem problem = Problem.builder()
                .withType(URI.create("locations/validation-error"))
                .withTitle("Validation error")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(exception.getMessage())
                .with("violation", violations)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);

    }


}
