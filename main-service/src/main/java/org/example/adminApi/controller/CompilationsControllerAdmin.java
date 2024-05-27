package org.example.adminApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.adminApi.service.compilation.CompilationsService;
import org.example.createAndUpdate.Create;
import org.example.createAndUpdate.Update;
import org.example.dto.request.CompilationsRequest;
import org.example.dto.response.CompilationsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class CompilationsControllerAdmin {

    private final CompilationsService compilationService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationsResponse crateCompilation(@Validated(Create.class) @RequestBody CompilationsRequest request) {
        log.info("CrateCompilation request {}", request);
        return compilationService.crateCompilation(request);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String removeCompilation(@PathVariable Long compId) {
        log.info("RemoveCompilation compId {}", compId);
        return compilationService.removeCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationsResponse updateCompilations(@PathVariable Long compId,
                                                   @Validated(Update.class) @RequestBody CompilationsRequest request) {
        log.info("UpdateCompilation compId {} request {}", compId, request);
        return compilationService.updateCompilations(compId, request);
    }
}
