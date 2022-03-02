package com.igd.xsltapi.controller;

import com.igd.xsltapi.payload.TransformationDto;
import com.igd.xsltapi.service.TransformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transformations")
@Api(value = "Transformations")
public class TransformationController {

    private final TransformationService transformationService;

    public TransformationController(TransformationService transformationService) {
        this.transformationService = transformationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Creation")
    public ResponseEntity<TransformationDto> createTransformation(@Valid @RequestBody TransformationDto transformationDto) {
        return new ResponseEntity<>(transformationService.createTransformation(transformationDto), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation(value = "All")
    public List<TransformationDto> getAllTransformations() {
        return transformationService.getAllTransformations();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Single")
    public ResponseEntity<TransformationDto> getTransformationById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(transformationService.getTransformationById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update")
    public ResponseEntity<TransformationDto> updateTransformationBy(@Valid @RequestBody TransformationDto transformationDto, @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(transformationService.updateTransformation(transformationDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Deletion")
    public ResponseEntity<String> deleteTransformation(@PathVariable(name = "id") long id) {
        transformationService.deleteTransformationById(id);
        return new ResponseEntity<>("Transformation successfully deleted", HttpStatus.OK);
    }
}
